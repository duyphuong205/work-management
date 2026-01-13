package com.cloud.work.entity;

import com.cloud.work.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Table(name = "user_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "status")
    String status;

    @Column(name = "email")
    String email;

    @Column(name = "role")
    String role;

    @Column(name = "password")
    String password;
}
