package com.example.reactive.learning;

import static java.lang.Integer.sum;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Traditional {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        var traditional = new Traditional();
        var start = Instant.now();
        traditional.executeMethod();
        var end = Instant.now();
        log.info("TimeTaken to execute sync :: {}", Duration.between(start, end));
        start = Instant.now();
        traditional.executeMethodAsync();
        end = Instant.now();
        log.info("TimeTaken to execute async :: {}", Duration.between(start, end));
        start = Instant.now();
        traditional.executeMethodReactive().block();
        end = Instant.now();
        log.info("TimeTaken to execute reactively :: {}", Duration.between(start, end));
    }

    private Mono<Integer> executeMethodReactive() {

        var scheduler = Schedulers.parallel();

        Supplier<Mono<Integer>> firstMethodSupplier =
                () -> Mono.fromCallable(this::firstMethod).log("first").subscribeOn(scheduler);

        Supplier<Mono<Integer>> secondMethodSupplier =
                () -> Mono.fromCallable(this::secondMethod).log("second").subscribeOn(scheduler);

        Supplier<Mono<Integer>> thirdMethodSupplier =
                () -> Mono.fromCallable(this::thirdMethod).log("third").subscribeOn(scheduler);

        return Mono.zip(
                        firstMethodSupplier.get(),
                        secondMethodSupplier.get(),
                        thirdMethodSupplier.get())
                .flatMap(
                        data -> {
                            Integer total = sum(data.getT1(), data.getT2());
                            Integer diff = diff(data.getT3(), data.getT1());

                            return Mono.zip(
                                            Mono.fromCallable(() -> divide(total, diff))
                                                    .log("divide")
                                                    .subscribeOn(scheduler),
                                            Mono.fromCallable(() -> multiply(total, diff))
                                                    .log("multiply")
                                                    .subscribeOn(scheduler))
                                    .flatMap(
                                            innerZip ->
                                                    Mono.just(innerZip.getT1() + innerZip.getT2()));
                        })
                .log("zip");
    }

    private int executeMethod() {
        int a = firstMethod();
        int b = secondMethod();
        Integer c = thirdMethod();

        Integer total = sum(a, b);

        Integer diff = diff(c, a);

        Integer d = divide(total, diff);

        Integer multi = multiply(total, diff);

        int value = d + multi;
        log.info("Value :{}", value);
        return value;
    }

    private Integer executeMethodAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(this::firstMethod);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(this::secondMethod);
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(this::thirdMethod);
        CompletableFuture.allOf(future1, future2, future3).join();

        Integer total = sum(future1.get(), future2.get());

        Integer diff = diff(future3.get(), future1.get());

        CompletableFuture<Integer> future4 =
                CompletableFuture.supplyAsync(() -> divide(total, diff));
        CompletableFuture<Integer> future5 =
                CompletableFuture.supplyAsync(() -> multiply(total, diff));

        Function<Void, Integer> function =
                (voidValue) -> {
                    try {
                        return future4.get() + future5.get();
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                };
        CompletableFuture<Integer> finalTotal =
                CompletableFuture.allOf(future4, future5).thenApplyAsync(function);
        log.info("Final Total :: {}", finalTotal.get());
        return finalTotal.get();
    }

    int divide(Integer total, Integer diff) {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("executed divide method");
        return total / diff;
    }

    int multiply(Integer total, Integer diff) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("executed multiply method");
        return total * diff;
    }

    Integer diff(Integer a, Integer b) {
        return a - b;
    }

    int firstMethod() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("executed 1st method");
        return 1;
    }

    int secondMethod() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("executed 2nd method");
        return 2;
    }

    int thirdMethod() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("executed 3rd method");
        return 3;
    }
}
