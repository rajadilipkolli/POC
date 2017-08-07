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


Adding DataSourceProxy will print SQL Queries as 

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

To Enable flyway include flyway core dependency and add `SQL` scripts under db/migration folder of resources folder of maven project

```xml
<dependency>
	<groupId>org.flywaydb</groupId>
	<artifactId>flyway-core</artifactId>
</dependency>
```

