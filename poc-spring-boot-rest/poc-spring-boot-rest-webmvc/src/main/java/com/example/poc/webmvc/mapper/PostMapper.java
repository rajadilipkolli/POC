package com.example.poc.webmvc.mapper;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.Records;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostComment;
import com.poc.restfulpoc.entities.PostDetails;
import com.poc.restfulpoc.entities.PostTag;
import com.poc.restfulpoc.entities.Tag;
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
    @Mapping(source = "createdOn", target = "createdOn")
    Post postDtoToPostIgnoringChild(PostDTO postDTO);

    @Mapping(target = "createdOn", ignore = true)
    PostComment postCommentsDTOToPostComment(Records.PostCommentsDTO postCommentsDTO);

    @Mapping(target = "id", ignore = true)
    Tag tagDTOToTag(Records.TagDTO tagDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createdOn", target = "createdOn")
    PostDetails postDTOToPostDetails(PostDTO postDTO);

    @Mapping(target = "details", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    void updateReferenceValues(PostDTO postDTO, @MappingTarget Post post);

    PostTag tagDTOToPostTag(Records.TagDTO tagDTO);

    Post postDtoToPost(PostDTO postDTO);

    List<PostComment> postCommentsDTOListToPostCommentList(List<Records.PostCommentsDTO> comments);

    List<Tag> tagDTOListToTagList(List<Records.TagDTO> tags);

    @Mapping(target = "name", source = "tag.name")
    Records.TagDTO postTagToTagDTO(PostTag postTag);
}
