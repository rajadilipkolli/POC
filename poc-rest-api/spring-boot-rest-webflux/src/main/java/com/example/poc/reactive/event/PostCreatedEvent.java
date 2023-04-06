/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.event;

import com.example.poc.reactive.entity.ReactivePost;

import org.springframework.context.ApplicationEvent;

import java.io.Serial;

public class PostCreatedEvent extends ApplicationEvent {

    @Serial private static final long serialVersionUID = 1L;

    public PostCreatedEvent(ReactivePost post) {
        super(post);
    }
}
