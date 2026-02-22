/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.batch;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobInvokerController {

    private final JobOperator jobLauncher;

    private final Job executionJob;

    @GetMapping("/api/run-batch-job")
    public String handle() throws Exception {

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addString("key", "Post")
                        .addDate("currentDate", new Date())
                        .toJobParameters();
        JobExecution jobExecution = this.jobLauncher.run(this.executionJob, jobParameters);

        return "Batch job has been invoked as " + jobExecution.getJobId();
    }
}
