package com.ufps.proyectoweb.services;

import com.ufps.proyectoweb.dto.user.LoginRequest;
import com.ufps.proyectoweb.dto.user.RegisterUserRequest;
import com.ufps.proyectoweb.dto.user.UserResponse;
import org.springframework.stereotype.Service;

/**
 * Provides the authentication use cases for the application.
 */
@Service
public class AuthService {

    private final UserService userService;

    /**
     * Creates the auth service.
     *
     * @param userService user service
     */
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param request registration data
     * @return created user response
     */
    public UserResponse register(RegisterUserRequest request) {
        return userService.register(request);
    }

    /**
     * Authenticates an existing user.
     *
     * @param request login data
     * @return authenticated user response
     */
    public UserResponse login(LoginRequest request) {
        return userService.authenticate(request);
    }

    /**
     * Ends the current session.
     */
    public void logout() {
        // Stateless API: nothing to invalidate yet.
    }
}

