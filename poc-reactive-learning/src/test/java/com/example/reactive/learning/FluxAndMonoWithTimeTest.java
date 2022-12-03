package com.example.reactive.learning;

import java.time.Duration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoWithTimeTest {

    @Test
    void infiniteSequence() throws InterruptedException {

        Flux<Long> infiniteFlux =
                Flux.interval(Duration.ofMillis(100)).log(); // starts from 0 --> ......

        infiniteFlux.subscribe((element) -> System.out.println("Value is : " + element));

        Thread.sleep(3000);
    }

    @Test
    void infiniteSequenceTest() {

        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100)).take(3).log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    void infiniteSequenceMap() {

        Flux<Integer> finiteFlux =
                Flux.interval(Duration.ofMillis(100)).map(Long::intValue).take(3).log();

        StepVerifier.create(finiteFlux).expectSubscription().expectNext(0, 1, 2).verifyComplete();
    }

    @Test
    @Disabled
    void infiniteSequenceMap_withDelay() {

        Flux<Integer> finiteFlux =
                Flux.interval(Duration.ofMillis(10))
                        .delayElements(Duration.ofSeconds(1))
                        .map(Long::intValue)
                        .take(3)
                        .log();

        StepVerifier.create(finiteFlux).expectSubscription().expectNext(0, 1, 2).verifyComplete();
    }
}
