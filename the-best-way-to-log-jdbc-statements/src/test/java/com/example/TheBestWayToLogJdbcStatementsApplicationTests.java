package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TheBestWayToLogJdbcStatementsApplicationTests
{

    @Autowired
    DataSource dataSource;

    @Test
    public void contextLoads() throws SQLException
    {
        assertThat(dataSource).isNotNull();
        assertThat(dataSource.getConnection().getSchema()).isNotEmpty();
    }

}
