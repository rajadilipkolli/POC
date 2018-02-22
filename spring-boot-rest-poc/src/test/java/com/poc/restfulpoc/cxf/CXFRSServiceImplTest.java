/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.cxf;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.entities.Customer;

class CXFRSServiceImplTest extends AbstractRestFulPOCApplicationTest {

    private static final String API_PATH = "/services/cxf";

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Test Customers")
    void testGetCustomers() throws Exception {
        final WebClient wc = WebClient.create("http://localhost:" + port + API_PATH);
        wc.accept("application/json");

        wc.path("/customers/");
        Response response = wc.get(Response.class);
        assertThat(response.getStatus()).isEqualTo(HttpURLConnection.HTTP_OK);
        String replyString = response.readEntity(String.class);
        final List<Customer> custList = convertJsonToCustomers(replyString);
        assertThat(custList).isNotEmpty().size().isGreaterThan(1);

        // Reverse to the starting URI
        wc.back(true);

        wc.path("/customers/").path(custList.get(0).getId());
        response = wc.get(Response.class);
        assertThat(response.getStatus()).isEqualTo(HttpURLConnection.HTTP_OK);
        replyString = response.readEntity(String.class);
        final ObjectMapper mapper = new ObjectMapper();
        final Customer cust = mapper.readValue(replyString, Customer.class);
        assertThat(replyString).isNotNull();
        assertThat(cust.getId()).isEqualTo(custList.get(0).getId());
    }

    private List<Customer> convertJsonToCustomers(String json) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, TypeFactory.defaultInstance()
                .constructCollectionType(List.class, Customer.class));
    }

}
