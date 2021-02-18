package com.mongodb.redis.integration.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;

@ControllerAdvice
final class ExceptionHandling implements ProblemHandling {}
