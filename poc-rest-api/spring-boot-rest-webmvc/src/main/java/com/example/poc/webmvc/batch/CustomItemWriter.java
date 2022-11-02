package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.dto.PostDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomItemWriter<L extends List<PostDTO>> implements ItemWriter<List<PostDTO>> {

    @Override
    public void write(List<? extends List<PostDTO>> items) {
        items.stream().flatMap(List::stream).forEach(item -> log.info("Writing Item :{}", item));
    }
}
