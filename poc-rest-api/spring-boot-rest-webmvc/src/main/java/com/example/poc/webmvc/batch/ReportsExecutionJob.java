/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.dto.PostDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ReportsExecutionJob implements JobExecutionListener {

    @Bean(name = "executionJob")
    public Job reportsExecutionJob(
            CustomItemReader<List<Long>> reader,
            CustomItemProcessor processor,
            CustomItemWriter<List<PostDTO>> writer,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {

        Step step =
                new StepBuilder("execution-step", jobRepository)
                        .allowStartIfComplete(true)
                        .<List<Long>, List<PostDTO>>chunk(2)
                        .reader(reader)
                        .processor(processor)
                        .writer(writer)
                        .transactionManager(transactionManager)
                        .build();

        return new JobBuilder("reporting-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(this)
                .start(step)
                .build();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("BATCH JOB COMPLETED SUCCESSFULLY");
        }
    }
}
