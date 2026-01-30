package com.cloud.work.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProjectRequest implements Serializable {
    Long projectId;
    @NotBlank(message = "{msg.project.name.not.blank}")
    String name;
}
