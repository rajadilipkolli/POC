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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 * <p>
 * Customer class.
 * </p>
 *
 * @author Raja Kolli
 * @version 1: 0
 */
@Setter
@Getter
@Entity
@ToString
@Builder
@NoArgsConstructor // Only to be compliant with JPA
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Required for Builder
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Getter
	@GenericGenerator(name = "sequenceGenerator", strategy = "enhanced-sequence",
			parameters = { @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
					@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
					@org.hibernate.annotations.Parameter(name = "increment_size", value = "5") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	private long id;

	@NotBlank(message = "Customer firstName is mandatory")
	@Column(name = "FIRST_NAME", nullable = false, unique = true)
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name = "DATE_OF_BIRTH")
	private LocalDateTime dateOfBirth;

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY,
			orphanRemoval = true)
	@JsonManagedReference
	private Address address;

	@Builder.Default
	@JsonManagedReference
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders = new ArrayList<>();

	/**
	 * <p>
	 * Setter for the field <code>address</code>.
	 * </p>
	 * @param address a {@link com.poc.restfulpoc.entities.Address} object.
	 */
	public void setAddress(Address address) {
		if (address == null) {
			if (this.address != null) {
				this.address.setCustomer(null);
			}
		}
		else {
			address.setCustomer(this);
		}
		this.address = address;
	}

	public void addOrder(Order order) {
		if (this.orders == null) {
			this.orders = new ArrayList<>();
		}
		this.orders.add(order);
		order.setCustomer(this);
	}

	public void removeOrder(Order order) {
		this.orders.remove(order);
		order.setCustomer(null);
	}

	public void setOrders(List<Order> orders) {
		if (orders != null) {
			for (Order order : orders) {
				addOrder(order);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Customer)) {
			return false;
		}
		Customer customer = (Customer) o;
		return Objects.equals(getFirstName(), customer.getFirstName())
				&& Objects.equals(getLastName(), customer.getLastName())
				&& Objects.equals(getDateOfBirth(), customer.getDateOfBirth())
				&& Objects.equals(getAddress(), customer.getAddress());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getFirstName(), getLastName(), getDateOfBirth(), getAddress());
	}

}
