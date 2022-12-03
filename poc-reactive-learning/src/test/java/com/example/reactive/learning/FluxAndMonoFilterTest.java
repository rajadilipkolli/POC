package com.example.reactive.learning;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoFilterTest {

    List<String> names = List.of("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest() {

        Flux<String> namesFlux =
                Flux.fromIterable(names) // adam, anna, jack,jenny
                        .filter(s -> s.startsWith("a"))
                        .log(); // adam,anna

        StepVerifier.create(namesFlux).expectNext("adam", "anna").verifyComplete();
    }

    @Test
    public void filterTestLength() {

        Flux<String> namesFlux =
                Flux.fromIterable(names) // adam, anna, jack,jenny
                        .filter(s -> s.length() > 4)
                        .log(); // jenny

        StepVerifier.create(namesFlux).expectNext("jenny").verifyComplete();
    }
}
