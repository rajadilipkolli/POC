/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.reactive.common.TestContainersConfig;
import com.example.poc.reactive.entity.ReactivePost;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@DataR2dbcTest(
        properties = {
            "spring.test.database.replace=none",
        })
@ImportTestcontainers(TestContainersConfig.class)
class ReactivePostRepositoryTest {

    @Autowired private PostRepository postRepository;

    @Autowired private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {

        Hooks.onOperatorDebug();

        List<String> statements =
                List.of( //
                        "DROP TABLE IF EXISTS reactive_posts;",
                        "CREATE TABLE reactive_posts ( id SERIAL PRIMARY KEY, "
                                + "title VARCHAR(100) NOT NULL, "
                                + "content VARCHAR(100) NOT NULL,"
                                + "created_by VARCHAR(100),"
                                + "updated_by VARCHAR(100) );");

        statements.forEach(
                statement ->
                        this.databaseClient
                                .sql(statement) //
                                .fetch() //
                                .rowsUpdated() //
                                .as(StepVerifier::create) //
                                .expectNextCount(1) //
                                .verifyComplete());
    }

    @Test
    public void testDatabaseClientExisted() {
        assertThat(databaseClient).isNotNull();
    }

    @Test
    public void testPostRepositoryExisted() {
        assertThat(postRepository).isNotNull();
    }

    @Test
    public void existedNoItemInPosts() {
        assertThat(this.postRepository.count().block()).isEqualTo(0);
    }

    @Test
    void executesFindAll() {

        ReactivePost dave = new ReactivePost("Dave", "Matthews");
        ReactivePost carter = new ReactivePost("Carter", "Beauford");

        insertPosts(dave, carter);

        this.postRepository
                .findAll() //
                .as(StepVerifier::create) //
                .assertNext(dave::equals) //
                .assertNext(carter::equals) //
                .verifyComplete();
    }

    @Test
    void executesAnnotatedQuery() {

        ReactivePost dave = new ReactivePost("Dave", "Matthews");
        ReactivePost carter = new ReactivePost("Carter", "Beauford");

        insertPosts(dave, carter);

        this.postRepository
                .findByContent("Matthews") //
                .as(StepVerifier::create) //
                .assertNext(dave::equals) //
                .verifyComplete();
    }

    private void insertPosts(ReactivePost... reactivePosts) {

        this.postRepository
                .saveAll(List.of(reactivePosts)) //
                .as(StepVerifier::create) //
                .expectNextCount(reactivePosts.length) //
                .verifyComplete();
    }
}
