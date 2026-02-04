package com.cloud.work.entity;

import com.cloud.work.entity.base.BaseEntity;
import com.cloud.work.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "project_member_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMemberInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "project_id")
    Long projectId;

    @Column(name = "email")
    String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    Role role;

    @CreationTimestamp
    @Column(name = "joined_date", updatable = false)
    Timestamp joinedDate;
}