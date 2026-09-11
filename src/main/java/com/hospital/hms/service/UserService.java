package com.hospital.hms.service;

import com.hospital.hms.dto.LoginRequest;
import com.hospital.hms.model.entity.User;

public interface UserService {
    boolean authenticate(LoginRequest loginRequest);
    User register(String username, String password);
}
