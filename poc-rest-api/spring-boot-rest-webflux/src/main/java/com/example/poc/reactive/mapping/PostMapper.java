/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.mapping;

import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ReactivePost toEntity(PostDto postDto);

    @InheritConfiguration
    void updatePost(PostDto postDto, @MappingTarget ReactivePost reactivePost);
}
