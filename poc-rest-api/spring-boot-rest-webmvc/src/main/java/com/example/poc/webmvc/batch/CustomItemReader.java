/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@StepScope
public class CustomItemReader<T> extends AbstractPagingItemReader<List<Long>> {

    private final PostRepository postRepository;

    private List<List<Long>> ids = new ArrayList<>();

    @Value("#{jobParameters['key']}")
    private String titleValue;

    public CustomItemReader(PostRepository postRepository) {
        this.postRepository = postRepository;
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
        if (!CollectionUtils.isEmpty(ids)) {
            results.add(this.ids.get(getPage() * getPageSize()));
        }
    }
}
