package com.example.reactive.learning;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling() {

        Flux<String> stringFlux =
                Flux.just("A", "B", "C")
                        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                        .concatWith(Flux.just("D"))
                        .onErrorResume(
                                (e) -> { // this block gets executed
                                    System.out.println("Exception is : " + e);
                                    return Flux.just("default", "default1");
                                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                // .expectError(RuntimeException.class)
                // .verify();
                .expectNext("default", "default1")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_OnErrorReturn() {

        Flux<String> stringFlux =
                Flux.just("A", "B", "C")
                        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                        .concatWith(Flux.just("D"))
                        .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap() {

        Flux<String> stringFlux =
                Flux.just("A", "B", "C")
                        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                        .concatWith(Flux.just("D"))
                        .onErrorMap(CustomException::new);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap_withRetry() {

        Flux<String> stringFlux =
                Flux.just("A", "B", "C")
                        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                        .concatWith(Flux.just("D"))
                        .onErrorMap(CustomException::new)
                        .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_OnErrorMap_withRetryBackoff() {

        Flux<String> stringFlux =
                Flux.just("A", "B", "C")
                        .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                        .concatWith(Flux.just("D"))
                        .onErrorMap(CustomException::new)
                        .retryWhen(Retry.backoff(2, Duration.ofSeconds(5)));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }
}
