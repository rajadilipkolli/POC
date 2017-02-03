package com.blog.samples.boot.rest.data;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.blog.samples.boot.rest.model.Address;
import com.blog.samples.boot.rest.model.Customer;

@Component
public class DataBuilder
{

    public List<Customer> createCustomers()
    {
        ZoneId defaultZoneId = ZoneId.of("UTC");
        Customer customer1 = new Customer("Raja", "Kolli",
                Date.from(LocalDate.of(1982, Month.JANUARY, 10)
                        .atStartOfDay(defaultZoneId).toInstant()),
                new Address("High Street", "Belfast", "India", "BT893PY"));

        Customer customer2 = new Customer("Paul", "Jones",
                Date.from(LocalDate.of(1973, Month.JANUARY, 03)
                        .atStartOfDay(defaultZoneId).toInstant()),
                new Address("Main Street", "Lurgan", "Armagh", "BT283FG"));

        Customer customer3 = new Customer("Steve", "Toale",
                Date.from(LocalDate.of(1979, Month.MARCH, 8).atStartOfDay(defaultZoneId)
                        .toInstant()),
                new Address("Main Street", "Newry", "Down", "BT359JK"));

        return Arrays.asList(customer1, customer2, customer3);
    }
}