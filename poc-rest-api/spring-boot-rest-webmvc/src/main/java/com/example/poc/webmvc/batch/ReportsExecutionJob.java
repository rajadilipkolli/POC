package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.dto.PostDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBatchProcessing
@RequiredArgsConstructor
public class ReportsExecutionJob extends JobExecutionListenerSupport {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "executionJob")
    public Job reportsExecutionJob(
            CustomItemReader<List<Long>> reader,
            CustomItemProcessor processor,
            CustomItemWriter<List<PostDTO>> writer) {

        Step step =
                this.stepBuilderFactory
                        .get("execution-step")
                        .allowStartIfComplete(true)
                        .<List<Long>, List<PostDTO>>chunk(1)
                        .reader(reader)
                        .processor(processor)
                        .writer(writer)
                        .build();

        return this.jobBuilderFactory
                .get("reporting-job")
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
