/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.mapper;

import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostRequestDTO;
import com.example.poc.webmvc.dto.TagDTO;
import com.example.poc.webmvc.entities.Post;
import com.example.poc.webmvc.entities.PostComment;
import com.example.poc.webmvc.entities.PostDetails;
import com.example.poc.webmvc.entities.PostTag;
import com.example.poc.webmvc.entities.Tag;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
@DecoratedWith(PostMapperDecorator.class)
public interface PostMapper {

    List<PostDTO> mapToPostDTOs(List<Post> postList);

    @Mapping(target = "createdBy", source = "details.createdBy")
    @Mapping(target = "createdOn", source = "details.createdOn")
    PostDTO mapPostToDTO(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "details", ignore = true)
    @Mapping(source = "createdOn", target = "updatedOn")
    Post postDtoToPostIgnoringChild(PostDTO postDTO);

    @Mapping(target = "createdOn", ignore = true)
    PostComment postCommentsDTOToPostComment(PostCommentsDTO postCommentsDTO);

    @Mapping(target = "id", ignore = true)
    Tag tagDTOToTag(TagDTO tagDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createdOn", target = "createdOn")
    PostDetails postDTOToPostDetails(PostDTO postDTO);

    @Mapping(target = "details", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    void updateReferenceValues(PostDTO postDTO, @MappingTarget Post post);

    PostTag tagDTOToPostTag(TagDTO tagDTO);

    Post postDtoToPost(PostDTO postDTO);

    List<PostComment> postCommentsDTOListToPostCommentList(List<PostCommentsDTO> comments);

    List<Tag> tagDTOListToTagList(List<TagDTO> tags);

    @Mapping(target = "name", source = "tag.name")
    TagDTO postTagToTagDTO(PostTag postTag);

    PostDTO postRequestDtoToPostDto(PostRequestDTO postRequestDTO);
}
