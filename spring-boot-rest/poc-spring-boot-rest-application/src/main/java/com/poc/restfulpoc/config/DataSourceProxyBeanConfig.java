/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.config;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

/**
 * <p>
 * DataSourceProxyBeanConfig class.
 * </p>
 *
 * @author Raja Kolli
 * @version 1: 0
 */
@Slf4j
@Configuration
public class DataSourceProxyBeanConfig implements BeanPostProcessor {

	/** {@inheritDoc} */
	@Override
	public Object postProcessAfterInitialization(final Object bean,
			final String beanName) {
		if (bean instanceof DataSource) {
			log.info("Inside Proxy Creation");
			final ProxyFactory factory = new ProxyFactory(bean);
			factory.setProxyTargetClass(true);
			factory.addAdvice(new ProxyDataSourceInterceptor((DataSource) bean));
			return factory.getProxy();
		}
		return bean;
	}

	private static class ProxyDataSourceInterceptor implements MethodInterceptor {

		private final DataSource dataSource;

		ProxyDataSourceInterceptor(final DataSource dataSource) {
			super();
			// use pretty formatted query with multiline enabled
			final PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
			creator.setMultiline(true);

			final SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
			listener.setQueryLogEntryCreator(creator);

			this.dataSource = ProxyDataSourceBuilder.create(dataSource).countQuery()
					.name("MyDS").listener(listener)
					.logSlowQueryToSysOut(5, TimeUnit.SECONDS).build();
		}

		@Override
		public Object invoke(final MethodInvocation invocation) throws Throwable {
			final Method proxyMethod = ReflectionUtils.findMethod(
					this.dataSource.getClass(), invocation.getMethod().getName());
			if (null != proxyMethod) {
				return proxyMethod.invoke(this.dataSource, invocation.getArguments());
			}
			return invocation.proceed();
		}

		/**
		 * Formatter for SQL Statements.
		 *
		 * @author Raja Kolli
		 *
		 */
		static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {

			private Formatter formatter = FormatStyle.BASIC.getFormatter();

			/** {@inheritDoc} */
			@Override
			protected String formatQuery(String query) {
				return this.formatter.format(query);
			}

		}

	}

}
