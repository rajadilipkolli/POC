/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Address class.
 * </p>
 *
 * @author Raja Kolli
 * @version 1: 0
 */
@Setter
@Getter
@Builder
@NoArgsConstructor // Only to be compliant with JPA
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Required for Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String street;

	private String town;

	private String county;

	private String postcode;

	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Address address = (Address) o;
		return Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getTown(), address.getTown())
				&& Objects.equals(getCounty(), address.getCounty())
				&& Objects.equals(getPostcode(), address.getPostcode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStreet(), getTown(), getCounty(), getPostcode());
	}

}
