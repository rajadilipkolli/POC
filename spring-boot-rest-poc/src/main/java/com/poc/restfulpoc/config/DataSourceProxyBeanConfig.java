/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Slf4j
@Configuration
public class DataSourceProxyBeanConfig implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {

        if (bean instanceof DataSource) {
            System.out.println("AfterInitialization : " + beanName);

            log.info("Inside Proxy Creation");

            // use pretty formatted query with multiline enabled
            final PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
            creator.setMultiline(true);

            final SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
            listener.setQueryLogEntryCreator(creator);

            return ProxyDataSourceBuilder.create((DataSource) bean).countQuery()
                    .name("MyDS").listener(listener)
                    .logSlowQueryToSysOut(1, TimeUnit.MINUTES).build();

        }
        return bean; // you can return any other object as well
    }
}
