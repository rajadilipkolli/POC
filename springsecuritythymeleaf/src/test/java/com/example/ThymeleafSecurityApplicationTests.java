package com.example;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThymeleafSecurityApplicationTests {

	/*@Autowired
	private ProductRepository productRepository;*/
/*	@Autowired
	private EmailService emailService;

	@Test
	public void testSendEmail() {
		emailService.sendEmail("rajadileepkolli@gmail.com", "JCart - Test Mail",
				"This is a test email from JCart");
	}

	@Test
	public void contextLoads() {
		assertThat(emailService).isNotNull();
	}

	@Test
	public void testSaveProduct() {
		productRepository.deleteAll();
		// setup product
		Product product = new Product();
		product.setDescription("Spring Framework Shirt");
		product.setPrice(new BigDecimal("18.95"));
		product.setProductId("1234");

		// save product, verify has ID value after save
		assertThat(product.getId()).isNull(); // null before save
		productRepository.save(product);
		assertThat(product.getId()).isNotNull(); // not null after save

		// fetch from DB
		Optional<Product> fetchedProductOptional = productRepository
				.findById(product.getId());

		Product fetchedProduct = fetchedProductOptional.get();
		// should not be null
		assertThat(fetchedProduct).isNotNull();

		// should equal
		assertThat(product.getId()).isEqualTo(fetchedProduct.getId());
		assertThat(product.getDescription()).isEqualTo(fetchedProduct.getDescription());

		// update description and save
		fetchedProduct.setDescription("New Description");
		productRepository.save(fetchedProduct);

		// get from DB, should be updated
		Optional<Product> fetchedUpdatedProduct = productRepository
				.findById(fetchedProduct.getId());

		assertThat(fetchedProduct.getDescription())
				.isEqualTo(fetchedUpdatedProduct.get().getDescription());

		// verify count of products in DB
		long productCount = productRepository.count();
		assertThat(productCount).isEqualTo(1);

		// get all products, list should only have one
		List<Product> products = productRepository.findAll();

		assertThat(products.size()).isEqualTo(1);
	}*/
}
