/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CustomItemReader<T> extends AbstractPagingItemReader<List<Long>> {

    private final PostRepository postRepository;

    private List<List<Long>> ids = new ArrayList<>();

    private String titleValue;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
        // use your parameters
        this.titleValue = parameters.getString("key");
    }

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
        if (this.ids.isEmpty()) {
            final AtomicInteger counter = new AtomicInteger();
            this.ids =
                    new ArrayList<>(
                            this.postRepository
                                    .findByTitleContaining("%" + this.titleValue + "%")
                                    .stream()
                                    .collect(
                                            Collectors.groupingBy(
                                                    it ->
                                                            counter.getAndIncrement()
                                                                    / getPageSize()))
                                    .values());
        }
        results.add(this.ids.get(getPage() * getPageSize()));
    }
}
