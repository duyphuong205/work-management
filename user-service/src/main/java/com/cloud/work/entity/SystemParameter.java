package com.cloud.work.entity;

import com.cloud.work.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Table(name = "system_parameter")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemParameter extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id")
    Long parameterId;

    @Column(name = "type")
    String type;

    @Column(name = "code")
    String code;

    @Column(name = "value")
    String value;

    @Column(name = "display_name")
    String displayName;

    @Column(name = "status")
    String status;
}
