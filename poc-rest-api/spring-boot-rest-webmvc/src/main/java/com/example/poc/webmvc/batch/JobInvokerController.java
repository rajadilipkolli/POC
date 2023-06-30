/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.batch;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobInvokerController {

    private final JobLauncher jobLauncher;

    private final Job executionJob;

    @GetMapping("/run-batch-job")
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
