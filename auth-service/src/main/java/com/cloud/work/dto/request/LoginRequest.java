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
public class LoginRequest implements Serializable {
    @NotBlank(message = "{msg.email.not.blank}")
    String email;

    @NotBlank(message = "{msg.password.not.blank}")
    String password;
}
