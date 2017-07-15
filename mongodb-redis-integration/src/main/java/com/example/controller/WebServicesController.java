/*
 * Copyright 2017 the original author or authors.
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

package com.example.controller;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Book;
import com.example.service.WebServicesService;

import lombok.RequiredArgsConstructor;

/**
 * @author rajakolli
 *
 */
@RestController
@RequestMapping(value = "/book")
@RequiredArgsConstructor
public class WebServicesController {

	private final WebServicesService service;

	@RequestMapping(value = "/saveBook", method = RequestMethod.POST)
	public Book saveBook(Book book) {
		return service.save(book);
	}

	@RequestMapping(value = "/findByTitle/{title}", method = RequestMethod.GET)
	@Cacheable(value = "book", key = "#title")
	public Book findBookByTitle(@PathVariable String title) {
		return service.findBookByTitle(title);
	}

	@RequestMapping(value = "/updateByTitle/{title}/{author}", method = RequestMethod.GET)
	@CachePut(value = "book", key = "#title")
	public Book updateByTitle(@PathVariable(value = "title") String title,
			@PathVariable(value = "author") String author) {
		return service.updateByTitle(title, author);
	}
}
