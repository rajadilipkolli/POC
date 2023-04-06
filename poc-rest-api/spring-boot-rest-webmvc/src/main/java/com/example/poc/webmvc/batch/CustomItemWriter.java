/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.dto.PostDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CustomItemWriter<L extends List<PostDTO>> implements ItemWriter<List<PostDTO>> {

    @Override
    public void write(Chunk<? extends List<PostDTO>> postChunks) {
        postChunks.getItems().forEach(postDTOS -> log.info("Writing Item :{}", postDTOS));
    }
}
