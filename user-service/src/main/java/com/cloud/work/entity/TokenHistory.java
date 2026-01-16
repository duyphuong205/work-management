package com.cloud.work.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "token_history")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    Long tokenId;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "token", columnDefinition = "TEXT")
    String token;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    String refreshToken;

    @Column(name = "status")
    String status;

    @Column(name = "expire_time")
    Timestamp expireTime;

    @Column(name = "refresh_expire_time")
    Timestamp refreshExpireTime;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    Timestamp createdTime;
}
