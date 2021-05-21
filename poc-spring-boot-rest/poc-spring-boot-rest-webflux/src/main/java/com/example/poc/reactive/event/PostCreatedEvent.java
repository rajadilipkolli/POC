package com.example.poc.reactive.event;

import com.example.poc.reactive.entity.ReactivePost;
import org.springframework.context.ApplicationEvent;

public class PostCreatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public PostCreatedEvent(ReactivePost post) {
        super(post);
    }
}
