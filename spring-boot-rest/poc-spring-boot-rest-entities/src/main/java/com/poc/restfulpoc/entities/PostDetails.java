/*
 * Copyright 2015-2020 the original author or authors.
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

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * PostDetails class.
 *
 * @author Raja Kolli
 * @since 0.2.1
 */
@Entity(name = "PostDetails")
@Table(name = "post_details")
@Getter
@Setter
public class PostDetails {

	@Id
	@GenericGenerator(name = "sequenceGenerator", strategy = "enhanced-sequence",
			parameters = { @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
					@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
					@org.hibernate.annotations.Parameter(name = "increment_size", value = "5") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	private Long id;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "created_by")
	private String createdBy;

	public PostDetails() {
		this.createdOn = LocalDateTime.now();
	}

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Post post;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PostDetails other = (PostDetails) obj;
		return Objects.equals(this.createdBy, other.createdBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.createdBy);
	}

}
