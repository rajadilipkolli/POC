/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.webmvc.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class SpringBootRestWebMvcIntegrationTest extends AbstractIntegrationTest {

    @Test
    void test() {
        assertThat(sqlContainer.isRunning()).isTrue();
    }
}
