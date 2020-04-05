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
package com.poc.reactivepoc.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import reactor.core.publisher.FluxSink;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class PostCreatedEventPublisher
		implements ApplicationListener<PostCreatedEvent>, Consumer<FluxSink<PostCreatedEvent>> {

	private final Executor executor;

	private final BlockingQueue<PostCreatedEvent> queue = new LinkedBlockingQueue<>();

	public PostCreatedEventPublisher(Executor executor) {
		this.executor = executor;
	}

	@Override
	public void accept(FluxSink<PostCreatedEvent> postCreatedEventFluxSink) {
		this.executor.execute(() -> {
			while (true) {
				try {
					PostCreatedEvent event = this.queue.take();
					postCreatedEventFluxSink.next(event);
				}
				catch (InterruptedException ex) {
					ReflectionUtils.rethrowRuntimeException(ex);
				}
			}
		});
	}

	@Override
	public void onApplicationEvent(PostCreatedEvent event) {
		this.queue.offer(event);
	}

}
