package com.cloud.work.entity;

import com.cloud.work.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Table(name = "message_template")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageTemplate extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    Long templateId;

    @Column(name = "type")
    String type;

    @Column(name = "title")
    String title;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    String content;

    @Column(name = "status")
    String status;

    @Column(name = "language")
    String language;
}
