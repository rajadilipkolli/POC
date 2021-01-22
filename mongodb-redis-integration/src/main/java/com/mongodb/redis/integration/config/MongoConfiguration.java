/*
 * Copyright 2015-2019 the original author or authors.
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

package com.mongodb.redis.integration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class MongoConfiguration {

	private final MongoTemplate mongoTemplate;

	@EventListener(ApplicationReadyEvent.class)
	public void initIndicesAfterStartup() {

		log.info("Mongo InitIndicesAfterStartup init");
		var init = System.currentTimeMillis();

		var mappingContext = this.mongoTemplate.getConverter().getMappingContext();

		if (mappingContext instanceof MongoMappingContext) {
			var resolver = IndexResolver.create(mappingContext);
			mappingContext.getPersistentEntities().stream().filter(clazz -> clazz.isAnnotationPresent(Document.class))
					.forEach(o -> {
						IndexOperations indexOps = this.mongoTemplate.indexOps(o.getType());
						resolver.resolveIndexFor(o.getType()).forEach(indexOps::ensureIndex);
					});
		}

		log.info("Mongo InitIndicesAfterStartup took: {}", (System.currentTimeMillis() - init));
	}

}
