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
package com.mongodb.redis.integration.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import com.mongodb.redis.integration.event.BookCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.FluxSink;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@Slf4j
public class BookCreatedEventPublisher
		implements ApplicationListener<BookCreatedEvent>, Consumer<FluxSink<BookCreatedEvent>> {

	private final Executor executor;

	private final BlockingQueue<BookCreatedEvent> queue = new LinkedBlockingQueue<>(); // <3>

	BookCreatedEventPublisher() {
		this.executor = Executors.newFixedThreadPool(20);
	}

	// <4>
	@Override
	public void onApplicationEvent(BookCreatedEvent event) {
		this.queue.offer(event);
	}

	@Override
	public void accept(FluxSink<BookCreatedEvent> sink) {
		this.executor.execute(() -> {
			while (true) {
				try {
					BookCreatedEvent event = this.queue.take(); // <5>
					sink.next(event); // <6>
				}
				catch (InterruptedException ex) {
					log.error("Interrupted Exception", ex);
					ReflectionUtils.rethrowRuntimeException(ex);
				}
			}
		});
	}

}
