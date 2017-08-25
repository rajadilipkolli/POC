# This project demonstrates how to use spring restful webservices deployed in undertow server, displaying SQL Query using database proxy

## Technology Stack

- Embedded undertow server
- JPA using Hibernate
- RestFul webservice
- [DatasourceProxy](https://github.com/ttddyy/datasource-proxy)
- [Flyway](https://flywaydb.org/)
- [ActiveMQ](http://activemq.apache.org/)

### Notes

We want to display all SQL queries so take advantage of spring boot autoconfiguration we will implement BeanPostProcessor and for the initilization of Bean configure DataSourceProxy

``` java
@Configuration
public class DataSourceProxyBeanConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) {
        if (bean instanceof DataSource) {
    			// write your datasource 
        }
    }
}
```


Adding DataSourceProxy will print SQL Queries as below

```
**********************************************************
Name:MyDS, Connection:7, Time:0, Success:True
Type:Prepared, Batch:True, QuerySize:1, BatchSize:3
Query:["
    insert 
    into
        address
        (county, postcode, street, town, id) 
    values
        (?, ?, ?, ?, ?)"]
Params:[(India,BT893PY,High Street,Belfast,2),(Armagh,BT283FG,Main Street,Lurgan,4),(Down,BT359JK,Main Street,Newry,6)]

Name:MyDS, Connection:7, Time:1, Success:True
Type:Prepared, Batch:True, QuerySize:1, BatchSize:3
Query:["
    insert 
    into
        customer
        (address_id, date_of_birth, first_name, last_name, id) 
    values
        (?, ?, ?, ?, ?)"]
Params:[(2,1982-01-10 05:30:00.0,Raja,Kolli,1),(4,1973-01-03 05:30:00.0,Paul,Jones,3),(6,1979-03-08 05:30:00.0,Steve,Toale,5)]
**********************************************************
```

To Enable flyway include flyway core dependency and add **SQL** scripts under db/migration folder of resources folder of maven project

```xml
<dependency>
	<groupId>org.flywaydb</groupId>
	<artifactId>flyway-core</artifactId>
</dependency>
```

To Enable ActiveMQ include spring-boot-starter-activemq starter and to produce JMS Message use 

`jmsTemplate.convertAndSend("jms.message.endpoint", message);`

where as to consume Message use in spring boot application.

```java
@Component
public class JMSReceiver {

    @JmsListener(destination = "jms.message.endpoint")
    public void receiveMessage(Object message) {
    }
}
```

### Exception Handling 
Reference :: [Guide to Spring Boot REST API Error Handling](https://www.toptal.com/java/spring-boot-rest-api-error-handling)

