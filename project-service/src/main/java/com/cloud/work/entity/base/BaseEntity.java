package com.cloud.work.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity implements Serializable {
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    Long createdBy;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    Timestamp createdTime;

    @LastModifiedBy
    @Column(name = "updated_by", insertable = false)
    Long updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_time", insertable = false)
    Timestamp updatedTime;
}
