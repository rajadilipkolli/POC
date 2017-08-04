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
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Slf4j
@Configuration
public class DataSourceProxyBeanConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName)
            throws BeansException {
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

            this.dataSource = ProxyDataSourceBuilder.create(dataSource).countQuery()
                    .name("MyDS").listener(listener)
                    .logSlowQueryToSysOut(1, TimeUnit.MINUTES).build();
        }

        @Override
        public Object invoke(final MethodInvocation invocation) throws Throwable {
            final Method proxyMethod = ReflectionUtils.findMethod(dataSource.getClass(),
                    invocation.getMethod().getName());
            if (proxyMethod != null) {
                return proxyMethod.invoke(dataSource, invocation.getArguments());
            }
            return invocation.proceed();
        }
    }
}
