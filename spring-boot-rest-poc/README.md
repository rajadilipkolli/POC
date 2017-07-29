# This project demonstrates how to use spring restful webservices deployed in undertow server, displaying SQL Query using database proxy

## Technology Stack

- Embedded undertow server
- JPA using Hibernate
- RestFul webservice
- DatasourceProxy
- flyway

### Notes

We want to display all SQL queries so take advantage of spring boot autoconfiguration we will implement BeanPostProcessor and for the initilization of Bean configure DataSourceProxy

```
@Configuration
public class DataSourceProxyBeanConfig implements BeanPostProcessor
```

To Enable flyway include flyway core dependency and add `SQL` scripts under db/migration folder of resources folder of maven project

```xml
<dependency>
	<groupId>org.flywaydb</groupId>
	<artifactId>flyway-core</artifactId>
</dependency>
```

