package com.cloud.work.dao.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskInfo implements Serializable {
    Long taskId;
    String title;
    String description;
    String comment;
    String status;
    Long assigneeId;
    Long projectId;
    Date startDate;
    Date end_date;
    Long createdBy;
    Timestamp createdTime;
    Long updatedBy;
    Timestamp updatedTime;
}
