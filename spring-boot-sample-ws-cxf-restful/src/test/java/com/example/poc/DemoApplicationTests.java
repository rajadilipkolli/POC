package com.example.poc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.client.core.WebServiceTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int serverPort;

	@Rule
	public OutputCapture output = new OutputCapture();

	private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

	@Test
	public void contextLoads() {
		assertThat(testRestTemplate).isNotNull();
	}

	@Test
	public void testCXFRestService() {
		ResponseEntity<String> response = this.testRestTemplate
				.getForEntity("/services/sayHello/{userName}", String.class, "ApacheCXF");
		assertThat(response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
		assertThat(response.getBody())
				.isEqualTo("Hello ApacheCXF, Welcome to CXF RS Spring Boot World!!!");
	}

	@Test
	public void testMVCRestService() {
		ResponseEntity<String> response = this.testRestTemplate
				.getForEntity("/restapi/example/{userName}", String.class, "SpringMVC");
		assertThat(response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
		assertThat(response.getBody()).isEqualTo("Welcome to Spring boot SpringMVC!!!");
	}

	@Test
	public void testHelloRequest() {
		final String request = "<q0:sayHello xmlns:q0=\"http://service.example.com/\"><myname>ApacheCXF</myname></q0:sayHello>";

		StreamSource source = new StreamSource(new StringReader(request));
		StreamResult result = new StreamResult(System.out);

		this.webServiceTemplate.setDefaultUri(
				"http://localhost:" + this.serverPort + "/services/SOAPWebService");
		this.webServiceTemplate.sendSourceAndReceiveToResult(source, result);
		assertThat(this.output.toString()).contains(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:sayHelloResponse xmlns:ns2=\"http://service.example.com/\"><return>Welcome to CXF Spring boot ApacheCXF!!!</return></ns2:sayHelloResponse>");
	}

}
