package com.example.reactive.learning;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.lang.Integer.sum;

public class Traditional {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Traditional traditional = new Traditional();
        Instant start = Instant.now();
//        traditional.executeMethod();
        Instant end = Instant.now();
        System.out.println("TimeTaken to execute sync :: " + Duration.between(start, end));
        start = Instant.now();
        traditional.executeMethodAsync();
        end = Instant.now();
        System.out.println("TimeTaken to execute async :: " + Duration.between(start, end));
        start = Instant.now();
        traditional.executeMethodReactive();
        end = Instant.now();
        System.out.println("TimeTaken to execute async :: " + Duration.between(start, end));
    }

    private Integer executeMethodReactive() {
        Scheduler scheduler = Schedulers.parallel();
        Mono<Integer> a = Mono.just(firstMethod());
        Mono<Integer> b = Mono.just(secondMethod());
        Mono<Integer> c = Mono.just(thirdMethod());

//        Flux<Integer> af = Flux.just(firstMethod()).subscribeOn(scheduler);
//        Flux<Integer> bf = Flux.just(secondMethod()).subscribeOn(scheduler);
//        Flux<Integer> cf = Flux.just(thirdMethod()).subscribeOn(scheduler);

        Mono<Integer> externalMono = Mono.zip(a, b, c).subscribeOn(scheduler).flatMap(data -> {
            Integer total = sum(data.getT1(), data.getT2());
            Integer diff = diff(data.getT3(), data.getT1());

            return Mono.zip(Mono.just(divide(total, diff)).subscribeOn(scheduler), Mono.just(multiply(total, diff)).subscribeOn(scheduler))
                    .flatMap(innerzip -> Mono.just(innerzip.getT1() + innerzip.getT2()));
        });

        Integer value = externalMono.block(Duration.ofMinutes(1));
        System.out.println(value);
        return value;

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
        System.out.println(value);
        return value;
    }

    private Integer executeMethodAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(this::firstMethod);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(this::secondMethod);
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(this::thirdMethod);
        CompletableFuture.allOf(future1, future2, future3).join();

        Integer total = sum(future1.get(), future2.get());

        Integer diff = diff(future3.get(), future1.get());

        CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> divide(total, diff));
        CompletableFuture<Integer> future5 = CompletableFuture.supplyAsync(() -> multiply(total, diff));

        Function<Void, Integer> function = (voidValue) -> {
            try {
                return future4.get() + future5.get();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        };
        CompletableFuture<Integer> finalTotal = CompletableFuture.allOf(future4, future5).thenApplyAsync(function);
        System.out.println("Final Total :: " + finalTotal.get());
        return finalTotal.get();
    }

    private int divide(Integer total, Integer diff) {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("divide");
        return total / diff;
    }

    private int multiply(Integer total, Integer diff) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("multiply");
        return total * diff;
    }

    private Integer diff(Integer a, Integer b) {
        return a - b;
    }

    private int firstMethod() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1");
        return 1;
    }

    private int secondMethod() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("2");
        return 2;
    }

    private int thirdMethod() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("3");
        return 3;
    }

}
