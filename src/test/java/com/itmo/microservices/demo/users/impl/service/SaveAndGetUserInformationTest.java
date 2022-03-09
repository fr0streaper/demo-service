package com.itmo.microservices.demo.users.impl.service;

import com.google.common.eventbus.EventBus;
import com.itmo.microservices.demo.users.api.model.AppUserModel;
import com.itmo.microservices.demo.users.api.model.RegistrationRequest;
import com.itmo.microservices.demo.users.api.service.UserService;
import com.itmo.microservices.demo.users.impl.entity.AppUser;
import com.itmo.microservices.demo.users.impl.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@SuppressWarnings("UnstableApiUsage")
public class SaveAndGetUserInformationTest {
    private AppUser appUser;
    private UUID globalUUID;

    UserService userService;

    @BeforeEach
    public void setUp() {
        appUser = new AppUser(
                "name",
                "password"
        );

        globalUUID = UUID.randomUUID();
        appUser.setId(globalUUID);

        var userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername("name")).thenReturn(appUser);
        when(userRepository.save(any())).thenReturn(appUser);

        var passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        var eventBus = mock(EventBus.class);
        var tokenManager = mock(JwtTokenManager.class);
        when(tokenManager.generateToken(any())).thenReturn("token");
        when(tokenManager.generateRefreshToken(any())).thenReturn("refreshToken");

        userService = new DefaultUserService(userRepository, passwordEncoder, eventBus, tokenManager);
    }

    private final RegistrationRequest request = new RegistrationRequest("name", "password");

    @Test
    public void registerTest() {
        userService.registerUser(request);
        AppUserModel user = new AppUserModel(
                globalUUID,
                "name",
                "password"
        );
        Assertions.assertEquals(user.getName(), userService.getUser("name").getUsername());
        Assertions.assertEquals(user.getPassword(), userService.getUser("name").getPassword());
    }

    @Test
    public void findUserTest() {
        AppUserModel user = new AppUserModel(
                globalUUID,
                "name",
                "password"
        );
        Assertions.assertEquals(user.getName(), userService.getUser("name").getUsername());
        Assertions.assertEquals(user.getPassword(), userService.getUser("name").getPassword());
        Assertions.assertNull(userService.getUser("anothername"));
    }
}
