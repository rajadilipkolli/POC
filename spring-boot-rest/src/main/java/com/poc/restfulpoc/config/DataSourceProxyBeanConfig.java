/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

/**
 * <p>DataSourceProxyBeanConfig class.</p>
 *
 * @author rajakolli
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

        public ProxyDataSourceInterceptor(final DataSource dataSource) {
            super();
            // use pretty formatted query with multiline enabled
            final PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
            creator.setMultiline(true);

            final SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
            listener.setQueryLogEntryCreator(creator);

            // @formatter:off
            this.dataSource = ProxyDataSourceBuilder.create(dataSource)
                                                    .countQuery()
                                                    .name("MyDS")
                                                    .listener(listener)
                                                    .logSlowQueryToSysOut(5, TimeUnit.SECONDS)
                                                    .build();
            // @formatter:on
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
    }
}

class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
    
    private Formatter formatter = FormatStyle.BASIC.getFormatter();

    /** {@inheritDoc} */
    @Override
    protected String formatQuery(String query) {
        return this.formatter.format(query);
    }

}
