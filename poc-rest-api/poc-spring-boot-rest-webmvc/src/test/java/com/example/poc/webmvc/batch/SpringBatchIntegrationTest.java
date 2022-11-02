package com.example.poc.webmvc.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.webmvc.common.AbstractIntegrationTest;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
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

    //    @AfterAll
    //    public void cleanUp() throws InterruptedException {
    //        TimeUnit.SECONDS.sleep(5);
    //        jobRepositoryTestUtils.removeJobExecutions();
    //    }

    private JobParameters defaultJobParameters() {
        return new JobParametersBuilder()
                .addString("key", "Post")
                .addDate("currentDate", new Date())
                .toJobParameters();
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
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo("COMPLETED");
    }
}
