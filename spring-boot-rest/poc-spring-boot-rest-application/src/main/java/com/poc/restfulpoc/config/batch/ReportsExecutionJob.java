/*
 * Copyright 2015-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.config.batch;

import java.util.List;

import com.poc.restfulpoc.dto.PostDTO;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBatchProcessing
@RequiredArgsConstructor
public class ReportsExecutionJob extends JobExecutionListenerSupport {

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor("spring_batch");
	}

	@Bean(name = "executionJob")
	public Job reportsExecutionJob(CustomItemReader<List<Long>> reader, CustomItemProcessor processor,
			CustomItemWriter<List<PostDTO>> writer, TaskExecutor taskExecutor) {

		Step step = this.stepBuilderFactory.get("execution-step").allowStartIfComplete(true)
				.<List<Long>, List<PostDTO>>chunk(1).reader(reader).processor(processor).writer(writer)
				.taskExecutor(taskExecutor).build();

		Job job = this.jobBuilderFactory.get("reporting-job").incrementer(new RunIdIncrementer()).listener(this)
				.start(step).build();

		return job;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("BATCH JOB COMPLETED SUCCESSFULLY");
		}
	}

}
