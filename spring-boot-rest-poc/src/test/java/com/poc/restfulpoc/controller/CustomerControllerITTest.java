package com.poc.restfulpoc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.poc.restfulpoc.RestFulPOCApplication;
import com.poc.restfulpoc.data.DataBuilder;
import com.poc.restfulpoc.entities.Address;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestFulPOCApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerControllerITTest {

    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private DataBuilder dataBuilder;

    private String base;

    @Before
    public void setUp() throws Exception {
        this.base = "/rest/customers";
        customerRepository.deleteAll();
        dataBuilder.run(null);
    }

    @Test
    public void testGetCustomerById() throws Exception {
        final Long customerId = getCustomerIdByFirstName("Raja");
        final ResponseEntity<String> response = template
                .getForEntity(String.format("%s/%s", base, customerId), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString())
                .isEqualTo(JSON_CONTENT_TYPE);

        final Customer customer = convertJsonToCustomer(response.getBody());

        assertThat(customer.getFirstName()).isEqualTo("Raja");
        assertThat(customer.getLastName()).isEqualTo("Kolli");
        assertThat(customer.getDateOfBirth().getTime()).isEqualTo(379468800000L);
        assertThat(customer.getAddress().getStreet()).isEqualTo("High Street");
        assertThat(customer.getAddress().getTown()).isEqualTo("Belfast");
        assertThat(customer.getAddress().getCounty()).isEqualTo("India");
        assertThat(customer.getAddress().getPostcode()).isEqualTo("BT893PY");
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        final ResponseEntity<String> response = template.getForEntity("/rest/customers",
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final List<Customer> customers = convertJsonToCustomers(response.getBody());
        assertThat(customers.size()).isEqualTo(3);
    }

    @Test
    public void testCreateCustomer() throws Exception {
        final Customer customer = new Customer("Gary", "Steale",
                Date.from(LocalDate.of(1984, Month.MARCH, 8)
                        .atStartOfDay(ZoneId.of("UTC")).toInstant()),
                new Address("Main Street", "Portadown", "Armagh", "BT359JK"));

        final ResponseEntity<String> response = template.postForEntity(base, customer,
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType().toString())
                .isEqualTo(JSON_CONTENT_TYPE);
        assertThat(response.getHeaders().getFirst("Location"))
                .contains("/rest/customers/");

        final Customer returnedCustomer = convertJsonToCustomer(response.getBody());
        assertThat(customer.getFirstName()).isEqualTo(returnedCustomer.getFirstName());
        assertThat(customer.getLastName()).isEqualTo(returnedCustomer.getLastName());
        assertThat(customer.getDateOfBirth())
                .isEqualTo(returnedCustomer.getDateOfBirth());
        assertThat(customer.getAddress().getStreet())
                .isEqualTo(returnedCustomer.getAddress().getStreet());
        assertThat(customer.getAddress().getTown())
                .isEqualTo(returnedCustomer.getAddress().getTown());
        assertThat(customer.getAddress().getCounty())
                .isEqualTo(returnedCustomer.getAddress().getCounty());
        assertThat(customer.getAddress().getPostcode())
                .isEqualTo(returnedCustomer.getAddress().getPostcode());
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        final Long customerId = getCustomerIdByFirstName("Raja");
        final ResponseEntity<String> getCustomerResponse = template
                .getForEntity(String.format("%s/%s", base, customerId), String.class);
        assertThat(getCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCustomerResponse.getHeaders().getContentType().toString())
                .isEqualTo(JSON_CONTENT_TYPE);

        final Customer returnedCustomer = convertJsonToCustomer(
                getCustomerResponse.getBody());
        assertThat(returnedCustomer.getFirstName()).isEqualTo("Raja");
        assertThat(returnedCustomer.getLastName()).isEqualTo("Kolli");
        assertThat(returnedCustomer.getDateOfBirth().getTime()).isEqualTo(379468800000L);
        assertThat(returnedCustomer.getAddress().getStreet()).isEqualTo("High Street");
        assertThat(returnedCustomer.getAddress().getTown()).isEqualTo("Belfast");
        assertThat(returnedCustomer.getAddress().getCounty()).isEqualTo("India");
        assertThat(returnedCustomer.getAddress().getPostcode()).isEqualTo("BT893PY");

        /* convert JSON response to Java and update name */
        final ObjectMapper mapper = new ObjectMapper();
        final Customer customerToUpdate = mapper.readValue(getCustomerResponse.getBody(),
                Customer.class);
        customerToUpdate.setFirstName("Wayne");
        customerToUpdate.setLastName("Rooney");

        /* PUT updated customer */
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<Customer> entity = new HttpEntity<Customer>(customerToUpdate,
                headers);
        final ResponseEntity<String> response = template.exchange(
                String.format("%s/%s", base, customerId), HttpMethod.PUT, entity,
                String.class, customerId);

        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        /* GET updated customer and ensure name is updated as expected */
        final ResponseEntity<String> getUpdatedCustomerResponse = template
                .getForEntity(String.format("%s/%s", base, customerId), String.class);
        assertThat(getCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCustomerResponse.getHeaders().getContentType().toString())
                .isEqualTo(JSON_CONTENT_TYPE);

        final Customer updatedCustomer = convertJsonToCustomer(
                getUpdatedCustomerResponse.getBody());
        assertThat(updatedCustomer.getFirstName()).isEqualTo("Wayne");
        assertThat(updatedCustomer.getLastName()).isEqualTo("Rooney");
        assertThat(updatedCustomer.getDateOfBirth().getTime()).isEqualTo(379468800000L);
        assertThat(updatedCustomer.getAddress().getStreet()).isEqualTo("High Street");
        assertThat(updatedCustomer.getAddress().getTown()).isEqualTo("Belfast");
        assertThat(updatedCustomer.getAddress().getCounty()).isEqualTo("India");
        assertThat(updatedCustomer.getAddress().getPostcode()).isEqualTo("BT893PY");
    }

    @Test
    public void testRemoveCustomer() throws Exception {
        final Long customerId = getCustomerIdByFirstName("Raja");
        final ResponseEntity<String> response = template
                .getForEntity(String.format("%s/%s", base, customerId), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString())
                .isEqualTo(JSON_CONTENT_TYPE);

        final Customer customer = convertJsonToCustomer(response.getBody());
        assertThat(customer.getFirstName()).isEqualTo("Raja");
        assertThat(customer.getLastName()).isEqualTo("Kolli");
        assertThat(customer.getDateOfBirth().getTime()).isEqualTo(379468800000L);
        assertThat(customer.getAddress().getStreet()).isEqualTo("High Street");
        assertThat(customer.getAddress().getTown()).isEqualTo("Belfast");
        assertThat(customer.getAddress().getCounty()).isEqualTo("India");
        assertThat(customer.getAddress().getPostcode()).isEqualTo("BT893PY");

        /* delete customer */
        template.delete(String.format("%s/%s", base, customerId), String.class);

        /* attempt to get customer and ensure qwe get a 404 */
        final ResponseEntity<String> secondCallResponse = template
                .getForEntity(String.format("%s/%s", base, customerId), String.class);
        assertThat(secondCallResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Convenience method for testing that gives us the customer id based on test
     * customers name. Need this as IDs will increment as tests are rerun
     * 
     * @param firstName
     * @return customer Id
     */
    private Long getCustomerIdByFirstName(String firstName) {
        return customerRepository.findByFirstName(firstName).stream().findAny().get()
                .getId();
    }

    private Customer convertJsonToCustomer(String json) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Customer.class);
    }

    private List<Customer> convertJsonToCustomers(String json) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, TypeFactory.defaultInstance()
                .constructCollectionType(List.class, Customer.class));
    }

}
