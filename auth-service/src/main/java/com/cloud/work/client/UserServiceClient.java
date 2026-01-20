package com.cloud.work.client;

import com.cloud.work.dto.response.AppResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:1111", path = "/api/v1/user")
public interface UserServiceClient {
    @GetMapping("/find-by-email")
    AppResponse getUserByEmail(@RequestParam("email") String email);
}
