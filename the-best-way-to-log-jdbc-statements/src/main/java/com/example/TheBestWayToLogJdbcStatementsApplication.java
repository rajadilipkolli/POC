package com.example;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

/**
 * Spring java based configuration using
 * {@link net.ttddyy.dsproxy.support.ProxyDataSourceBuilder}.
 *
 * @author rajakolli
 */
@SpringBootApplication
public class TheBestWayToLogJdbcStatementsApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(TheBestWayToLogJdbcStatementsApplication.class);
    }

    @Bean
    public DataSource actualDataSource()
    {
        EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
        return databaseBuilder.setType(EmbeddedDatabaseType.H2).build();
    }

    // use hibernate to format queries
    private static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator
    {
        private Formatter formatter = FormatStyle.BASIC.getFormatter();

        @Override
        protected String formatQuery(String query)
        {
            return this.formatter.format(query);
        }
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSource actualDataSource)
    {
        // use pretty formatted query with multiline enabled
        PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
        creator.setMultiline(true);

        // SLF4JQueryLoggingListener listener = new SLF4JQueryLoggingListener();
        SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
        listener.setQueryLogEntryCreator(creator);

        // @formatter:off
        return ProxyDataSourceBuilder
                    .create(actualDataSource)
                    .name("MyDS")
                    .listener(listener)
                    .countQuery()
                    .logSlowQueryToSysOut(10, TimeUnit.MINUTES) // also by sl4j, jul, commons
                    .build();
    
        // @formatter:on
    }

    @Bean
    CommandLineRunner init(JdbcTemplate jdbcTemplate)
    { // DataSourceAutoConfiguration creates jdbcTemplate
        return args ->
            {

                System.out.println(
                        "**********************************************************");

                jdbcTemplate.execute("CREATE TABLE users (id INT, name VARCHAR(20))");

                jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)",
                        Arrays.asList(new Object[][] { { 1, "foo" }, { 2, "bar" } }));

                PreparedStatement preparedStatement = jdbcTemplate.getDataSource()
                        .getConnection()
                        .prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
                preparedStatement.setString(2, "FOO");
                preparedStatement.setInt(1, 3);
                preparedStatement.addBatch();
                preparedStatement.setInt(1, 4);
                preparedStatement.setString(2, "BAR");
                preparedStatement.addBatch();
                preparedStatement.executeBatch();

                jdbcTemplate.queryForObject("SELECT COUNT(1) FROM users", Integer.class);
                System.out.println(
                        "**********************************************************");

            };
    }

}