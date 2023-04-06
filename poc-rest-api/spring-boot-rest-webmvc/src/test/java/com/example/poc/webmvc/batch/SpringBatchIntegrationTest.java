/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.webmvc.common.AbstractIntegrationTest;

import org.junit.jupiter.api.*;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;

@SpringBatchTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SpringBatchIntegrationTest extends AbstractIntegrationTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired private Job jobUnderTest;

    @BeforeEach
    public void setup() {
        this.jobRepositoryTestUtils.removeJobExecutions();
        this.jobLauncherTestUtils.setJob(this.jobUnderTest);
    }

    @Test
    public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
        // given

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        assertThat(actualJobInstance.getJobName()).isEqualTo("reporting-job");
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
        assertThat(actualJobExitStatus).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    public void testMyJob() throws Exception {
        // given
        JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParameters();

        // when
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    private JobParameters defaultJobParameters() {
        return new JobParametersBuilder()
                .addString("key", "Post")
                .addDate("currentDate", new Date())
                .toJobParameters();
    }
}
