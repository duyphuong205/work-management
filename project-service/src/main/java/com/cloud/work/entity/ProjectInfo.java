package com.cloud.work.entity;

import com.cloud.work.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "project_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    Long projectId;

    @Column(name = "name")
    String name;

    @Column(name = "status")
    String status;

    @Column(name = "owner_id")
    Long ownerId;
}