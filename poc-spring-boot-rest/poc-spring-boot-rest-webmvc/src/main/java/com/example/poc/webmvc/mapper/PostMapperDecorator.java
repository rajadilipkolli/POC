package com.example.poc.webmvc.mapper;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.Records;
import com.example.poc.webmvc.repository.PostCommentRepository;
import com.example.poc.webmvc.repository.TagRepository;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostComment;
import com.poc.restfulpoc.entities.PostTag;
import com.poc.restfulpoc.entities.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PostMapperDecorator implements PostMapper {

    @Autowired private PostCommentRepository postCommentRepository;

    @Autowired
    @Qualifier("delegate")
    private PostMapper postMapperDelegate;

    @Autowired private TagRepository tagRepository;

    @Override
    public Tag tagDTOToTag(Records.TagDTO tagDTO) {
        if (tagDTO == null) {
            return null;
        }

        Tag tag = new Tag();

        tag.setName(tagDTO.name());

        return this.tagRepository.save(tag);
    }

    @Override
    public PostTag tagDTOToPostTag(Records.TagDTO tagDTO) {
        if (tagDTO == null) {
            return null;
        }

        PostTag postTag = new PostTag();
        postTag.setTag(new Tag(tagDTO.name()));

        return postTag;
    }

    @Override
    public Post postDtoToPost(PostDTO postDTO) {
        if (postDTO == null) {
            return null;
        }

        Post post = postDtoToPostIgnoringChild(postDTO);
        post.addDetails(postDTOToPostDetails(postDTO));
        addPostCommentsToPost(postDTO.getComments(), post);
        addPostTagsToPost(postDTO.getTags(), post);

        return post;
    }

    void addPostTagsToPost(List<Records.TagDTO> tags, Post post) {
        if (tags == null) {
            return;
        }

        for (Records.TagDTO tagDTO : tags) {
            Optional<Tag> tag = this.tagRepository.findByName(tagDTO.name());
            if (tag.isPresent()) {
                PostTag postTag = new PostTag(post, tag.get());
                post.getTags().add(postTag);
            } else {
                post.addTag(tagDTOToTag(tagDTO));
            }
        }
    }

    void addPostCommentsToPost(List<Records.PostCommentsDTO> comments, Post post) {

        if (comments == null) {
            return;
        }

        for (Records.PostCommentsDTO postCommentsDTO : comments) {
            post.addComment(postCommentsDTOToPostComment(postCommentsDTO));
        }
    }

    @Override
    public void updateReferenceValues(PostDTO postDTO, Post post) {
        if (postDTO == null) {
            return;
        }

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        if (post.getComments() != null) {
            if (null != postDTO.getComments() && !postDTO.getComments().isEmpty()) {
                // convertPostCommentsDTO to PostComment Entities
                List<PostComment> updatePostCommentsRequest =
                        this.postMapperDelegate.postCommentsDTOListToPostCommentList(
                                postDTO.getComments());

                // Remove the existing database rows that are no
                // longer found in the incoming collection (postCommentRequested)
                List<PostComment> postCommentsToRemove =
                        post.getComments().stream()
                                .filter(
                                        postComment ->
                                                !updatePostCommentsRequest.contains(postComment))
                                .collect(Collectors.toList());
                postCommentsToRemove.forEach(post::removeComment);

                // Update the existing database rows which can be found
                // in the incoming collection (updateCustomerRequest.getOrders())
                List<PostComment> newPostComments =
                        updatePostCommentsRequest.stream()
                                .filter(postComment -> !post.getComments().contains(postComment))
                                .collect(Collectors.toList());

                updatePostCommentsRequest.stream()
                        .filter(postComment -> !newPostComments.contains(postComment))
                        .forEach(
                                (postComment) -> {
                                    postComment.setPost(post);
                                    PostComment mergedPostComment =
                                            this.postCommentRepository.save(postComment);
                                    post.getComments()
                                            .set(
                                                    post.getComments().indexOf(mergedPostComment),
                                                    mergedPostComment);
                                });

                // Add the rows found in the incoming collection,
                // which cannot be found in the current database snapshot
                newPostComments.forEach(post::addComment);
            } else {
                post.getComments().forEach(post::removeComment);
            }
        } else {
            List<PostComment> list =
                    this.postMapperDelegate.postCommentsDTOListToPostCommentList(
                            postDTO.getComments());
            if (list != null) {
                list.forEach(post::addComment);
            }
        }
        if (post.getTags() != null) {
            if (null != postDTO.getTags() && !postDTO.getTags().isEmpty()) {

                // convertPostCommentsDTO to PostComment Entities
                List<Tag> updateTagsRequest =
                        this.postMapperDelegate.tagDTOListToTagList(postDTO.getTags());

                List<Tag> existingTags =
                        post.getTags().stream().map(PostTag::getTag).collect(Collectors.toList());

                // Remove the existing database rows that are no
                // longer found in the incoming collection (updateTagsRequest)
                List<Tag> tagsToRemoveList =
                        existingTags.stream()
                                .filter(tag -> !updateTagsRequest.contains(tag))
                                .collect(Collectors.toList());
                tagsToRemoveList.forEach(post::removeTag);

                List<String> tagNames =
                        existingTags.stream().map(Tag::getName).collect(Collectors.toList());
                // Update the existing database rows which can be found
                // in the incoming collection (updateTagsRequest)
                List<Tag> newTagsList =
                        updateTagsRequest.stream()
                                .filter(tag -> !tagNames.contains(tag.getName()))
                                .collect(Collectors.toList());

                // Add the rows found in the incoming collection,
                // which cannot be found in the current database snapshot
                newTagsList.forEach(post::addTag);

            } else {
                post.getTags().forEach(postTag -> post.removeTag(postTag.getTag()));
            }
        } else {
            List<Tag> list1 = this.postMapperDelegate.tagDTOListToTagList(postDTO.getTags());
            if (list1 != null) {
                list1.forEach(post::addTag);
            }
        }
    }
}
