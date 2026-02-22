/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.webmvc.common.AbstractIntegrationTest;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@SpringBatchTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SpringBatchIntegrationTest extends AbstractIntegrationTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired private Job jobUnderTest;

    @BeforeEach
    void setup() {
        this.jobRepositoryTestUtils.removeJobExecutions();
        this.jobLauncherTestUtils.setJob(this.jobUnderTest);
    }

    @Test
    void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
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
    void testMyJob() throws Exception {
        // given
        JobParameters jobParameters =
                new JobParametersBuilder(this.jobLauncherTestUtils.getUniqueJobParameters())
                        .addString("key", "Post")
                        .toJobParameters();

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
