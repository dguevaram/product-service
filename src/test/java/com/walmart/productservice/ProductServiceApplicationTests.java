package com.walmart.productservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.productservice.dto.ProductRequest;
import com.walmart.productservice.repository.IProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.3");

	@Autowired
	private MockMvc mockMvc;

	//This class will let us convert a JSON object to a POJO and vice versa
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private IProductRepository productRepository;

	@DynamicPropertySource
	private static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	public void cleanMongoData() {
		productRepository.deleteAll();
	}

	@Test
	public void createProduct() throws Exception {
		ProductRequest productRequest = defaultProductRequest();
		String productRequestJson = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestJson))
               .andExpect(status().isCreated());
		assertEquals(1, productRepository.findAll().size());
	}


	@Test
	public void createProductList() throws Exception {
		List<ProductRequest> productRequestList = createProductRequestList();
		String productRequestListJson = objectMapper.writeValueAsString(productRequestList);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create/list")
											  .contentType(MediaType.APPLICATION_JSON)
											  .content(productRequestListJson))
						.andExpect(status().isCreated());
		assertEquals(3, productRepository.findAll().size());
	} 

	@Test
	public void getAllProducts() throws Exception {
		List<ProductRequest> productRequestList = createProductRequestList();
		String productRequestListJson = objectMapper.writeValueAsString(productRequestList);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create/list")
											  .contentType(MediaType.APPLICATION_JSON)
											  .content(productRequestListJson))
						.andExpect(status().isCreated());
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	private ProductRequest defaultProductRequest(){
		return ProductRequest.builder()
							 .name("iPhone 13")
							 .description("iPhone 13")
							 .price(BigDecimal.valueOf(1200)).build();
	}

	private ProductRequest createProductRequest(String name, String description, BigDecimal price){
		return new ProductRequest(name, description, price);
	}

	private List<ProductRequest> createProductRequestList(){
		List<ProductRequest> productRequestList = new ArrayList<>();
		for(int i=0; i<3; i++){
			String numb = String.valueOf(i);
			productRequestList.add(createProductRequest("name "+numb, "description "+numb, BigDecimal.valueOf(Math.random())));
		}
		return productRequestList;
	}
}
