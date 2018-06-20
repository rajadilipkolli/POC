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

package com.poc.restfulpoc.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	private Long id;

	private String title;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
	private List<PostComment> comments = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true, fetch = FetchType.LAZY)
	private PostDetails details;

	@ManyToMany
	@JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<>();

	public Post(Long id) {
		this.id = id;
	}

	public Post(String title) {
		this.title = title;
	}

	public void addComment(PostComment comment) {
		this.comments.add(comment);
		comment.setPost(this);
	}

	public void addDetails(PostDetails details) {
		this.details = details;
		details.setPost(this);
	}

	public void removeDetails() {
		this.details.setPost(null);
		this.details = null;
	}

}
