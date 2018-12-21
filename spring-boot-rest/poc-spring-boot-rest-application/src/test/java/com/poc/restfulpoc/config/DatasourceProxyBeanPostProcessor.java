package com.poc.restfulpoc.config;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@TestComponent
@Slf4j
public class DatasourceProxyBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {

		if (bean instanceof DataSource) {

			log.info("Inside Proxy Creation :{}", bean);

			final ProxyFactory proxyFactory = new ProxyFactory(bean);

			proxyFactory.setProxyTargetClass(true);
			proxyFactory.addAdvice(new ProxyDataSourceInterceptor((DataSource) bean));

			return proxyFactory.getProxy();
		}
		return bean;
	}

	private static class ProxyDataSourceInterceptor implements MethodInterceptor {

		private final DataSource dataSource;

		public ProxyDataSourceInterceptor(final DataSource dataSource) {
			super();
			this.dataSource = ProxyDataSourceBuilder.create(dataSource)
					.name("DATA_SOURCE_PROXY").logQueryBySlf4j(SLF4JLogLevel.INFO)
					.multiline().countQuery().logSlowQueryBySlf4j(5, TimeUnit.SECONDS)
					.build();
		}

		@Override
		public Object invoke(final MethodInvocation invocation) throws Throwable {

			final Method proxyMethod = ReflectionUtils.findMethod(
					this.dataSource.getClass(), invocation.getMethod().getName());

			if (proxyMethod != null) {
				return proxyMethod.invoke(this.dataSource, invocation.getArguments());
			}

			return invocation.proceed();
		}

	}

}
