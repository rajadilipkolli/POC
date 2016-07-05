package com.blog.samples.boot.rest.controller.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.blog.samples.Application;
import com.blog.samples.boot.rest.data.DataBuilder;
import com.blog.samples.boot.rest.model.Address;
import com.blog.samples.boot.rest.model.Customer;
import com.blog.samples.boot.rest.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class CustomerControllerIT {

	@Value("${local.server.port}")
	private int port;
	private URL base;
	private RestTemplate template;

	@Autowired
	private DataBuilder dataBuilder;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8"; 
	
	
	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/rest/customers");
		template = new TestRestTemplate();		
		
		/* remove and reload test data */
		customerRepository.deleteAll();		
		dataBuilder.createCustomers().forEach(customer -> customerRepository.save(customer));		
	}

	@Test
	public void getAllCustomers() throws Exception {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);		
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		
		List<Customer> customers = convertJsonToCustomers(response.getBody());		
		assertThat(customers.size(), equalTo(3));		
	}
	
	@Test
	public void getCustomerById() throws Exception {
		
		Long customerId = getCustomerIdByFirstName("Raja");
		ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base.toString(), customerId), String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		Customer customer = convertJsonToCustomer(response.getBody());
		
		assertThat(customer.getFirstName(), equalTo("Raja"));
		assertThat(customer.getLastName(), equalTo("Kolli"));
		assertThat(customer.getDateOfBirth().toString(), equalTo("Sun Jan 10 00:00:00 IST 1982"));
		assertThat(customer.getAddress().getStreet(), equalTo("High Street"));
		assertThat(customer.getAddress().getTown(), equalTo("Belfast"));
		assertThat(customer.getAddress().getCounty(), equalTo("India"));
		assertThat(customer.getAddress().getPostcode(), equalTo("BT893PY"));
	}
	
	@Test
	public void createCustomer() throws Exception {

		Customer customer = new Customer("Gary", "Steale", DateTime.parse("1984-03-08").toDate(),
				new Address("Main Street", "Portadown", "Armagh", "BT359JK"));

		ResponseEntity<String> response = template.postForEntity("http://localhost:" + port + "/rest/customers", customer, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
		assertThat(response.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		assertThat(response.getHeaders().getFirst("Location"), containsString("/rest/customers/"));
		
		Customer returnedCustomer = convertJsonToCustomer(response.getBody());		
		assertThat(customer.getFirstName(), equalTo(returnedCustomer.getFirstName()));
		assertThat(customer.getLastName(), equalTo(returnedCustomer.getLastName()));
		assertThat(customer.getDateOfBirth(), equalTo(returnedCustomer.getDateOfBirth()));
		assertThat(customer.getAddress().getStreet(), equalTo(returnedCustomer.getAddress().getStreet()));
		assertThat(customer.getAddress().getTown(), equalTo(returnedCustomer.getAddress().getTown()));
		assertThat(customer.getAddress().getCounty(), equalTo(returnedCustomer.getAddress().getCounty()));
		assertThat(customer.getAddress().getPostcode(), equalTo(returnedCustomer.getAddress().getPostcode()));
	}

	@Test
	public void updateCustomer() throws Exception {

		Long customerId = getCustomerIdByFirstName("Raja");
		ResponseEntity<String> getCustomerResponse = template.getForEntity(String.format("%s/%s", base.toString(), customerId), String.class);
		assertThat(getCustomerResponse.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(getCustomerResponse.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		Customer returnedCustomer = convertJsonToCustomer(getCustomerResponse.getBody());
		assertThat(returnedCustomer.getFirstName(), equalTo("Raja"));
		assertThat(returnedCustomer.getLastName(), equalTo("Kolli"));
		assertThat(returnedCustomer.getDateOfBirth().toString(), equalTo("Sun Jan 10 00:00:00 IST 1982"));
		assertThat(returnedCustomer.getAddress().getStreet(), equalTo("High Street"));
		assertThat(returnedCustomer.getAddress().getTown(), equalTo("Belfast"));
		assertThat(returnedCustomer.getAddress().getCounty(), equalTo("India"));
		assertThat(returnedCustomer.getAddress().getPostcode(), equalTo("BT893PY"));
		
		/* convert JSON response to Java and update name */
		ObjectMapper mapper = new ObjectMapper();
		Customer customerToUpdate = mapper.readValue(getCustomerResponse.getBody(), Customer.class);
		customerToUpdate.setFirstName("Wayne");
		customerToUpdate.setLastName("Rooney");

		/* PUT updated customer */
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON); 
		HttpEntity<Customer> entity = new HttpEntity<Customer>(customerToUpdate, headers); 
		ResponseEntity<String> response = template.exchange(String.format("%s/%s", base.toString(), customerId), HttpMethod.PUT, entity, String.class, customerId);
		
		assertThat(response.getBody(), nullValue());
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));

		/* GET updated customer and ensure name is updated as expected */
		ResponseEntity<String> getUpdatedCustomerResponse = template.getForEntity(String.format("%s/%s", base.toString(), customerId), String.class);
		assertThat(getCustomerResponse.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(getCustomerResponse.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		Customer updatedCustomer = convertJsonToCustomer(getUpdatedCustomerResponse.getBody());
		assertThat(updatedCustomer.getFirstName(), equalTo("Wayne"));
		assertThat(updatedCustomer.getLastName(), equalTo("Rooney"));
		assertThat(updatedCustomer.getDateOfBirth().toString(), equalTo("Sun Jan 10 00:00:00 IST 1982"));
		assertThat(updatedCustomer.getAddress().getStreet(), equalTo("High Street"));
		assertThat(updatedCustomer.getAddress().getTown(), equalTo("Belfast"));
		assertThat(updatedCustomer.getAddress().getCounty(), equalTo("India"));
		assertThat(updatedCustomer.getAddress().getPostcode(), equalTo("BT893PY"));
	}

	@Test
	public void deleteCustomer() throws Exception {

		Long customerId = getCustomerIdByFirstName("Raja");		
		ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base.toString(), customerId), String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), equalTo(JSON_CONTENT_TYPE));
		
		Customer customer = convertJsonToCustomer(response.getBody());
		assertThat(customer.getFirstName(), equalTo("Raja"));
		assertThat(customer.getLastName(), equalTo("Kolli"));
		assertThat(customer.getDateOfBirth().toString(), equalTo("Sun Jan 10 00:00:00 IST 1982"));
		assertThat(customer.getAddress().getStreet(), equalTo("High Street"));
		assertThat(customer.getAddress().getTown(), equalTo("Belfast"));
		assertThat(customer.getAddress().getCounty(), equalTo("India"));
		assertThat(customer.getAddress().getPostcode(), equalTo("BT893PY"));
		
		/* delete customer */
		template.delete(String.format("%s/%s", base.toString(), customerId), String.class);
		
		/* attempt to get customer and ensure qwe get a 404 */
		ResponseEntity<String> secondCallResponse = template.getForEntity(String.format("%s/%s", base.toString(), customerId), String.class);
		assertThat(secondCallResponse.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void getNonExistantCustomerReturnsError404() throws Exception {
		
		ResponseEntity<String> response = template.getForEntity(String.format("%s/%s", base.toString(), 999999), String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));		
	}
	
	/**
	 * Convenience method for testing that gives us the customer 
	 * id based on test customers name. Need this as IDs will increment
	 * as tests are rerun
	 * 
	 * @param firstName
	 * @return customer Id
	 */
	private Long getCustomerIdByFirstName(String firstName){
		return customerRepository.findByFirstName(firstName).stream().findAny().get().getId();
	}
	
	private Customer convertJsonToCustomer(String json) throws Exception {		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, Customer.class);
	}
	
	private List<Customer> convertJsonToCustomers(String json) throws Exception {		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, Customer.class));
	}
}