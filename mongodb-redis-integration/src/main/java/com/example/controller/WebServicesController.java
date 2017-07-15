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

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@PostMapping(value = "/saveBook")
	public Book saveBook(Book book) {
		return service.save(book);
	}

	/**
	 * unless is specified to not cache null values
	 * 
	 * @param title
	 * @return
	 */
	@GetMapping(value = "/findByTitle/{title}")
	@Cacheable(value = "book", key = "#title", unless = "#result == null")
	public Book findBookByTitle(@PathVariable String title) {
		return service.findBookByTitle(title);
	}

	@PutMapping(value = "/updateByTitle/{title}/{author}")
	@CachePut(value = "book", key = "#title")
	public Book updateByTitle(@PathVariable(value = "title") String title,
			@PathVariable(value = "author") String author) {
		return service.updateByTitle(title, author);
	}

	@DeleteMapping(value = "/deleteByTitle/{title}")
	@CacheEvict(value = "book", key = "#title")
	public String deleteBookByTitle(@PathVariable(value = "title") String title) {
		Book book = this.findBookByTitle(title);
		if (null != book) {
			this.service.deleteBook(book.getId());
			return "Book with title " + title + " deleted";
		}
		else {
			return "Book with title " + title + " Not Found";
		}
	}

	/**
	 * Deletes all cache
	 */
	@GetMapping(value = "/deleteCache")
	@CacheEvict(value = "book", allEntries = true)
	public void deleteCache() {
		this.service.deleteAllCache();
	}

	public Long count() {
		return this.service.count();
	}

	public void deleteAll() {
		this.service.deleteAllCollections();
	}
}
