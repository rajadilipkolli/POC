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

package org.poc.springboot.mongodb.security.repository;

import org.poc.springboot.mongodb.security.domain.Role;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Role Document in MongoDB Mapping Operations.
 *
 * @author Raja Kolli
 * @since 0.2.1
 *
 */
public interface RoleRepository extends MongoRepository<Role, String> {

	Role findByRole(String string);

}
