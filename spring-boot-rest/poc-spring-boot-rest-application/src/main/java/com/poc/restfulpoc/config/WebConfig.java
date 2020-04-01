/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * defines Web Requests related configuration.
 *
 * @author Raja Kolli
 *
 */
@Configuration
public class WebConfig {

	private static final String PLACEHOLDER_METHOD_NAME = "$[methodName]";

	private static final String PLACEHOLDER_ARGUMENTS = "$[arguments]";

	private static final String PLACEHOLDER_TARGET_CLASS_NAME = "$[targetClassName]";

	private static final String PLACEHOLDER_INVOCATION_TIME = "$[invocationTime]";

	/**
	 * Creates a trace interceptor for logging entry into and exit from methods.
	 * @return the created trace interceptor
	 */
	@Bean
	public CustomizableTraceInterceptor customizableTraceInterceptor() {
		CustomizableTraceInterceptor cti = new CustomizableTraceInterceptor();
		cti.setUseDynamicLogger(true);
		cti.setEnterMessage("Entering method '" + PLACEHOLDER_METHOD_NAME + "(" + PLACEHOLDER_ARGUMENTS
				+ ")' of class [" + PLACEHOLDER_TARGET_CLASS_NAME + "]");
		cti.setExitMessage("Exiting method '" + PLACEHOLDER_METHOD_NAME + "' of class [" + PLACEHOLDER_TARGET_CLASS_NAME
				+ "] took " + PLACEHOLDER_INVOCATION_TIME + "ms.");
		return cti;
	}

	@Bean
	public Advisor traceAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public * com.poc.restfulpoc..*.*(..))");
		return new DefaultPointcutAdvisor(pointcut, customizableTraceInterceptor());
	}

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();
		crlf.setIncludeClientInfo(true);
		crlf.setIncludeQueryString(true);
		crlf.setIncludePayload(true);
		return crlf;
	}

}
