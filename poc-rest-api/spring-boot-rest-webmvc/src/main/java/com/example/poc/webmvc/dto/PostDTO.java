/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Schema(description = "All details related to posts.")
public class PostDTO extends RepresentationModel<PostDTO> {

    @Schema(description = "Title is mandatory", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    private String content;

    private String createdBy;

    private LocalDateTime createdOn;

    @Builder.Default private List<PostCommentsDTO> comments = new ArrayList<>();

    @Builder.Default private List<TagDTO> tags = new ArrayList<>();
}
