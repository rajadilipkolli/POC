package com.example.poc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests
{

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void contextLoads()
    {
        assertThat(testRestTemplate).isNotNull();
    }

    @Test
    public void testCXFRestService()
    {
        ResponseEntity<String> response = this.testRestTemplate
                .getForEntity("/services/sayHello/{userName}", String.class, "ApacheCXF");
        assertThat(response.getStatusCode().is2xxSuccessful()).isEqualTo(true);
    }

}
