/* Licensed under Apache-2.0 2025 */
package com.example.poc.webmvc.service.impl;

import static com.example.poc.webmvc.testcontainersflyway.db.tables.Post.POST;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.PostComment.POST_COMMENT;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.PostDetails.POST_DETAILS;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.PostTag.POST_TAG;
import static com.example.poc.webmvc.testcontainersflyway.db.tables.Tag.TAG;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostRequestDTO;
import com.example.poc.webmvc.dto.TagDTO;
import com.example.poc.webmvc.service.PostService;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service(value = "jooqPostService")
public class JooqPostServiceImpl implements PostService {

    private final DSLContext dsl;

    public JooqPostServiceImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     *
     *
     * {@snippet :
     * String generatedQuery =
     *
     * """
     * SELECT
     * "public"."post"."title"              ,
     * "public"."post"."content"            ,
     * "public"."post_details"."created_by" ,
     * "public"."post_details"."created_on" ,
     * (
     * SELECT
     * COALESCE(jsonb_agg(jsonb_build_array("v0")), jsonb_build_array())
     * FROM
     * (
     * SELECT
     * "public"."post_comment"."review" AS "v0"
     * FROM
     * "public"."post_comment"
     * WHERE
     * "public"."post_comment"."post_id" = "public"."post"."id" ) AS "t" ) AS "comments" ,
     * (
     * SELECT
     * COALESCE(jsonb_agg(jsonb_build_array("v0")), jsonb_build_array())
     * FROM
     * (
     * SELECT
     * "public"."tag"."name" AS "v0"
     * FROM
     * "public"."tag"
     * JOIN
     * "public"."post_tag"
     * ON
     * "public"."post"."id" = "public"."post_tag"."post_id"
     * WHERE
     * "public"."post_tag"."tag_id" = "public"."tag"."id" ) AS "t" ) AS "tags"
     * FROM
     * public"."post"
     * JOIN
     * "public"."post_details"
     * ON
     * "public"."post"."id" = "public"."post_details"."post_id"
     * WHERE
     * "public"."post_details"."created_by" = '?'
     * """;
     * }
     *
     * @param userName name of post createdBy
     * @return list of posts
     */
    @Override
    @Cacheable(value = "posts", key = "#userName", unless = "#result == null")
    public List<PostDTO> fetchAllPostsByUserName(String userName) {

        return dsl.select(
                        POST.TITLE,
                        POST.CONTENT,
                        POST_DETAILS.CREATED_BY,
                        POST_DETAILS.CREATED_ON,
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
                        POST_DETAILS.CREATED_ON,
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
