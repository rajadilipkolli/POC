package com.example.reactive.learning;

import static reactor.core.scheduler.Schedulers.parallel;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoTransformTest {
    List<String> names = List.of("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {

        Flux<String> namesFlux =
                Flux.fromIterable(names)
                        .map(String::toUpperCase) // ADAM, ANNA, JACK, JENNY
                        .log();

        StepVerifier.create(namesFlux).expectNext("ADAM", "ANNA", "JACK", "JENNY").verifyComplete();
    }

    @Test
    public void transformUsingMap_Length() {

        Flux<Integer> namesFlux =
                Flux.fromIterable(names)
                        .map(String::length) // ADAM, ANNA, JACK, JENNY
                        .log();

        StepVerifier.create(namesFlux).expectNext(4, 4, 4, 5).verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_repeat() {

        Flux<Integer> namesFlux =
                Flux.fromIterable(names)
                        .map(String::length) // ADAM, ANNA, JACK, JENNY
                        .repeat(1)
                        .log();

        StepVerifier.create(namesFlux).expectNext(4, 4, 4, 5, 4, 4, 4, 5).verifyComplete();
    }

    @Test
    public void transformUsingMap_Filter() {

        Flux<String> namesFlux =
                Flux.fromIterable(names)
                        .filter(s -> s.length() > 4)
                        .map(String::toUpperCase) // JENNY
                        .log();

        StepVerifier.create(namesFlux).expectNext("JENNY").verifyComplete();
    }

    @Test
    public void tranformUsingFlatMap() {

        Flux<String> stringFlux =
                Flux.fromIterable(List.of("A", "B", "C", "D", "E", "F")) // A, B, C, D, E, F
                        .flatMap(
                                s -> {
                                    return Flux.fromIterable(
                                            convertToList(
                                                    s)); // A -> List[A, newValue] , B -> List[B,
                                    // newValue]
                                }) // db or external service call that returns a flux -> s ->
                        // Flux<String>
                        .log();

        StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();
    }

    private List<String> convertToList(String s) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of(s, "newValue");
    }

    @Test
    public void tranformUsingFlatMap_usingparallel() {

        Flux<String> stringFlux =
                Flux.fromIterable(List.of("A", "B", "C", "D", "E", "F")) // Flux<String>
                        .window(2) // Flux<Flux<String> -> (A,B), (C,D), (E,F)
                        .flatMap(
                                (s) ->
                                        s.map(this::convertToList)
                                                .subscribeOn(parallel())) // Flux<List<String>
                        .flatMap(Flux::fromIterable) // Flux<String>
                        .log();

        StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();
    }

    @Test
    public void tranformUsingFlatMap_parallel_maintain_order() {

        Flux<String> stringFlux =
                Flux.fromIterable(List.of("A", "B", "C", "D", "E", "F")) // Flux<String>
                        .window(2) // Flux<Flux<String> -> (A,B), (C,D), (E,F)
                        /* .concatMap((s) ->
                        s.map(this::convertToList).`(parallel())) */
                        // Flux<List<String>
                        .flatMapSequential(
                                (s) -> s.map(this::convertToList).subscribeOn(parallel()))
                        .flatMap(Flux::fromIterable) // Flux<String>
                        .log();

        StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();
    }
}
