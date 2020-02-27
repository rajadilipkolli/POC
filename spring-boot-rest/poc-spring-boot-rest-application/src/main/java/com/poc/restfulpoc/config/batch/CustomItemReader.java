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

package com.poc.restfulpoc.config.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.poc.restfulpoc.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomItemReader<T> extends AbstractPagingItemReader<List<Long>> {

	private final PostRepository postRepository;

	private volatile List<List<Long>> ids = new ArrayList<>();

	private String titleValue;

	@BeforeStep
	public void beforeStep(final StepExecution stepExecution) {
		JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
		// use your parameters
		this.titleValue = parameters.getString("key");
	}

	@Override
	protected void doReadPage() {
		if (results == null) {
			results = new CopyOnWriteArrayList<>();
		}
		else {
			results.clear();
		}
		if (this.ids.isEmpty()) {
			final AtomicInteger counter = new AtomicInteger();
			this.ids = new ArrayList<>(this.postRepository.findByTitleContaining("%" + this.titleValue + "%").stream()
					.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / getPageSize())).values());
		}
		results.add(this.ids.get(getPage() * getPageSize()));
	}

	@Override
	protected void doJumpToPage(int itemIndex) {

	}

}
