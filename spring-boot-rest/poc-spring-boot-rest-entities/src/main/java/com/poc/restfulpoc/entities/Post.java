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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Post Entity Class.
 *
 * @author Raja Kolli
 * @since 0.2.1
 *
 */
@Entity(name = "Post")
@Table(name = "post")
@NoArgsConstructor
@Getter
@Setter
public class Post {

	@Id
	@GenericGenerator(name = "sequenceGenerator", strategy = "enhanced-sequence",
			parameters = { @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
					@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
					@org.hibernate.annotations.Parameter(name = "increment_size", value = "5") })
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	private Long id;

	private String title;

	private String content;

	private LocalDateTime createdOn;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
	private List<PostComment> comments = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true, fetch = FetchType.LAZY)
	private PostDetails details;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostTag> tags = new ArrayList<>();

	public Post(Long id) {
		this.id = id;
		this.createdOn = LocalDateTime.now();
	}

	public Post(String title) {
		this.title = title;
		this.createdOn = LocalDateTime.now();
	}

	public void addComment(PostComment comment) {
		this.comments.add(comment);
		comment.setPost(this);
	}

	public void removeComment(PostComment comment) {
		this.comments.remove(comment);
		comment.setPost(null);
	}

	public void addDetails(PostDetails details) {
		this.details = details;
		details.setPost(this);
	}

	public void removeDetails() {
		this.details.setPost(null);
		this.details = null;
	}

	public void addTag(Tag tag) {
		PostTag postTag = new PostTag(this, tag);
		this.tags.add(postTag);
		tag.getPosts().add(postTag);
	}

	public void removeTag(Tag tag) {
		for (Iterator<PostTag> iterator = this.tags.iterator(); iterator.hasNext();) {
			PostTag postTag = iterator.next();

			if (postTag.getPost().equals(this) && postTag.getTag().equals(tag)) {
				iterator.remove();
				postTag.getTag().getPosts().remove(postTag);
				postTag.setPost(null);
				postTag.setTag(null);
			}
		}
	}

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
		Post other = (Post) obj;
		return Objects.equals(this.details, other.details) && Objects.equals(this.title, other.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.details, this.title);
	}

}
