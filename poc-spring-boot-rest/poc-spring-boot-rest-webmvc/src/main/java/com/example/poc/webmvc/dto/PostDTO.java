package com.example.poc.webmvc.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO extends RepresentationModel<PostDTO> {

    @NotBlank(message = "Title of post is mandatory")
    private String title;

    private String content;

    private String createdBy;

    private String createdOn;

    @Builder.Default private List<Records.PostCommentsDTO> comments = new ArrayList<>();

    @Builder.Default private List<Records.TagDTO> tags = new ArrayList<>();
}
