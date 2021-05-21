package com.example.poc.webmvc.service.impl;

import static com.poc.restfulpoc.jooq.Tables.POST;
import static com.poc.restfulpoc.jooq.Tables.POST_COMMENT;
import static com.poc.restfulpoc.jooq.Tables.POST_DETAILS;
import static com.poc.restfulpoc.jooq.Tables.POST_TAG;
import static com.poc.restfulpoc.jooq.Tables.TAG;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostRequestDTO;
import com.example.poc.webmvc.service.PostService;
import java.util.Collections;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service(value = "jooqPostService")
public class JooqPostServiceImpl implements PostService {

    private final DSLContext dsl;

    public JooqPostServiceImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<PostDTO> fetchAllPostsByUserName(String userName) {
        return Collections.emptyList();
    }

    @Override
    public PostDTO fetchPostByUserNameAndTitle(String userName, String title) {

        return dsl.select(POST.TITLE, POST.CONTENT, POST_DETAILS.CREATED_BY, POST.CREATEDON)
                //                        POST_COMMENT.REVIEW,
                //                        TAG.NAME)
                .from(POST)
                .join(POST_DETAILS)
                .on(POST.ID.eq(POST_DETAILS.POST_ID))
                .join(POST_COMMENT)
                .on(POST.ID.eq(POST_COMMENT.POST_ID))
                .join(POST_TAG)
                .on(POST.ID.eq(POST_TAG.POST_ID))
                .join(TAG)
                .on(POST_TAG.TAG_ID.eq(TAG.ID))
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
