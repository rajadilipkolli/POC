/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.cxf;

import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.entities.Customer;
import org.apache.commons.lang3.RandomUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raja Kolli
 *
 */
class CXFRSServiceImplTest extends AbstractRestFulPOCApplicationTest {

	private static final String API_PATH = "/services/cxf";

	@Autowired
	private ObjectMapper mapper;

	@LocalServerPort
	private int port;

	@Test
	@DisplayName("Test Customers")
	void testGetCustomers() throws Exception {
		final WebClient wc = WebClient.create("http://localhost:" + port + API_PATH,
				"username", "password", null);
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
		final Customer cust = this.mapper.readValue(replyString, Customer.class);
		assertThat(replyString).isNotNull();
		assertThat(cust.getId()).isEqualTo(custList.get(0).getId());

		// Reverse to the starting URI
		wc.back(true);
		wc.path("/customers/").path(RandomUtils.nextLong(1000, 10000));
		response = wc.get(Response.class);
		assertThat(response.getStatus()).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND);
	}

}
