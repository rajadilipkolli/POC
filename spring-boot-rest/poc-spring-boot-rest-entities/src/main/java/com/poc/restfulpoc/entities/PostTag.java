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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "PostTag")
@Table(name = "post_tag")
@Setter
@Getter
public class PostTag {

	@EmbeddedId
	private PostTagId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("postId")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("tagId")
	private Tag tag;

	@Column(name = "created_on")
	private LocalDateTime createdOn = LocalDateTime.now();

	public PostTag() {
		this.createdOn = LocalDateTime.now();
	}

	public PostTag(Post post, Tag tag) {
		this.post = post;
		this.tag = tag;
		this.id = new PostTagId(post.getId(), tag.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PostTag that = (PostTag) o;
		return Objects.equals(this.post, that.post) && Objects.equals(this.tag, that.tag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.post, this.tag);
	}

}
