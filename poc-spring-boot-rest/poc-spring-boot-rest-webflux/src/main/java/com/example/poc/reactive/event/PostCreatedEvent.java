package com.example.poc.reactive.event;

import com.example.poc.reactive.entity.ReactivePost;
import org.springframework.context.ApplicationEvent;

public class PostCreatedEvent extends ApplicationEvent {

    public PostCreatedEvent(ReactivePost post) {
        super(post);
    }
}
