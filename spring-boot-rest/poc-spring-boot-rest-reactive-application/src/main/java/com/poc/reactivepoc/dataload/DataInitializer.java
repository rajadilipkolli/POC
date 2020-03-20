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

package com.poc.reactivepoc.dataload;

import java.util.Arrays;
import java.util.List;

import com.poc.reactivepoc.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

	private final DatabaseClient databaseClient;

	@EventListener(ContextRefreshedEvent.class)
	public void init() {
		log.info("start data initialization  ...");
		List<String> statements = Arrays.asList(//
				"DROP TABLE IF EXISTS reactive_posts;",
				"CREATE TABLE reactive_posts ( id SERIAL PRIMARY KEY, title VARCHAR(100) NOT NULL, content VARCHAR(100) NOT NULL);");

		statements.forEach(query -> this.databaseClient.execute(query).then().block());

		this.databaseClient.delete().from("reactive_posts").then()
				.and(this.databaseClient.insert().into("reactive_posts").value("title", "First post title")
						.value("content", "Content of my first post").map((r, m) -> r.get("id", Integer.class)).all()
						.log())
				.thenMany(this.databaseClient.select().from("reactive_posts").orderBy(Sort.by(Order.desc("id")))
						.as(Post.class).fetch().all().log())
				.subscribe(null, e -> log.error(e.getMessage(), e), () -> log.info("initialization is done..."));
	}

}
