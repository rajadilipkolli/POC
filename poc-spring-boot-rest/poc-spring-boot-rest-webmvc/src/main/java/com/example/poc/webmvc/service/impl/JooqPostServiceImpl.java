package com.example.poc.webmvc.service.impl;

import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostRequestDTO;
import com.example.poc.webmvc.dto.TagDTO;
import com.example.poc.webmvc.service.PostService;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.poc.webmvc.testcontainersflyway.db.tables.Post.POST;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.PostComment.POST_COMMENT;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.PostDetails.POST_DETAILS;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.PostTag.POST_TAG;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.Tag.TAG;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

@Service(value = "jooqPostService")
public class JooqPostServiceImpl implements PostService {

  private final DSLContext dsl;

  public JooqPostServiceImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  @Cacheable(value = "posts", key = "#userName", unless = "#result == null")
  public List<PostDTO> fetchAllPostsByUserName(String userName) {
    return dsl.select(
            POST.TITLE,
            POST.CONTENT,
            POST_DETAILS.CREATED_BY,
            POST.CREATED_ON,
            multiset(
                    select(POST_COMMENT.REVIEW)
                        .from(POST_COMMENT)
                        .where(POST_COMMENT.POST_ID.eq(POST.ID)))
                .as("comments")
                .convertFrom(r -> r.map(mapping(PostCommentsDTO::new))),
            multiset(
                    select(TAG.NAME)
                        .from(TAG)
                        .join(POST_TAG)
                        .on(POST.ID.eq(POST_TAG.POST_ID))
                        .where(POST_TAG.TAG_ID.eq(TAG.ID)))
                .as("tags")
                .convertFrom(r -> r.map(mapping(TagDTO::new))))
        .from(POST)
        .join(POST_DETAILS)
        .on(POST.ID.eq(POST_DETAILS.POST_ID))
        .where(POST_DETAILS.CREATED_BY.eq(userName))
        .fetch(mapping(PostDTO::new));
  }

  @Override
  @Cacheable(value = "posts", key = "#userName+#title", unless = "#result == null")
  public PostDTO fetchPostByUserNameAndTitle(String userName, String title) {

    return dsl.select(
            POST.TITLE,
            POST.CONTENT,
            POST_DETAILS.CREATED_BY,
            POST.CREATED_ON,
            multiset(
                    select(POST_COMMENT.REVIEW)
                        .from(POST_COMMENT)
                        .where(POST_COMMENT.POST_ID.eq(POST.ID)))
                .as("comments")
                .convertFrom(r -> r.map(mapping(PostCommentsDTO::new))),
            multiset(
                    select(TAG.NAME)
                        .from(TAG)
                        .join(POST_TAG)
                        .on(POST.ID.eq(POST_TAG.POST_ID))
                        .where(POST_TAG.TAG_ID.eq(TAG.ID)))
                .as("tags")
                .convertFrom(r -> r.map(mapping(TagDTO::new))))
        .from(POST)
        .join(POST_DETAILS)
        .on(POST.ID.eq(POST_DETAILS.POST_ID))
        .where(POST_DETAILS.CREATED_BY.eq(userName).and(POST.TITLE.eq(title)))
        .fetchOneInto(PostDTO.class);
  }

  @Override
  public void deletePostByIdAndUserName(String userName, String title) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PostDTO updatePostByUserNameAndId(PostDTO postDTO, String title) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void createPost(PostRequestDTO postRequestDTO, String userName) {
    throw new UnsupportedOperationException();
  }
}
