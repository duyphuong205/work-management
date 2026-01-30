package com.cloud.work.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProjectResponse implements Serializable {
    Long projectId;
    String name;
    String status;
    String updatedTime;
}
