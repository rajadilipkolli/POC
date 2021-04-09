package com.example.reactive.learning;

/**
 * create can be very useful to bridge an existing API
 * with the reactive world - such as an asynchronous API based on listeners.
 */
// Given I have a Async API like the following interface.
interface MyEventListener<T> {
    T onDataChunk(T chunk);
    void processComplete();
}
