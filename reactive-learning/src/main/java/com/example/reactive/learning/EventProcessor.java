package com.example.reactive.learning;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.function.Consumer;

// Create a Simple Async API
// Defining it here so that test can run independently
public class EventProcessor {

    public MyEventListener<Integer> l;

    public void register(MyEventListener<Integer> listener) {
        this.l = listener;
    }

    public void process() throws InterruptedException {
        Arrays.asList(1,2,3,4,5)
                .forEach(i -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    l.onDataChunk(i);
                });
        Thread.sleep(2000);
        l.processComplete();
    }

    public void process(Flux<Integer> a, Flux<Integer> b, Flux<Integer> c) {
        Traditional traditional = new Traditional();
        Scheduler scheduler = Schedulers.parallel();
        Flux.zip(a, b, c)
                .parallel(3)
                .runOn(scheduler)
                .flatMap(data -> {
                    Integer total = Integer.sum(data.getT1(), data.getT2());
                    Integer diff = traditional.diff(data.getT3(), data.getT1());

                    return Mono.zip(Mono.just(traditional.divide(total, diff))
                            .subscribeOn(scheduler), Mono.just(traditional.multiply(total, diff)).subscribeOn(scheduler))
                            .flatMap(inner -> Mono.just(inner.getT1() + inner.getT2()));
                })
                .subscribe(integer -> {
                    System.out.println("finalValue :: " + integer);
                    l.onDataChunk(integer);
                });

        l.processComplete();
    }
}
