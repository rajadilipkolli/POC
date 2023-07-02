/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

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
        this.executor.execute(
                () -> {
                    while (true) {
                        try {
                            PostCreatedEvent event = this.queue.take();
                            postCreatedEventFluxSink.next(event);
                        } catch (InterruptedException ex) {
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
