package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.dto.LoginRequest;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.dto.UserDto;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.enums.UserRole;
import com.example.E_Commerce.exception.InvalidCredentialsException;
import com.example.E_Commerce.exception.NotFoundException;
import com.example.E_Commerce.mapper.EntityDtoMapper;
import com.example.E_Commerce.repository.UserRepo;
import com.example.E_Commerce.security.JwtUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        // Initialize UserDto
        userDto = new UserDto();
        userDto.setName("CK");
        userDto.setEmail("yck11214@gmail.com");
        userDto.setPassword("12345");
        userDto.setRole("user");

        // Initialize User entity
        user = User.builder()
                .name("CK")
                .email("yck11214@gmail.com")
                .password("passwordEncoder")
                .role(UserRole.USER)
                .build();

        // Initialize UserServiceImpl with mocks
        userService = new UserServiceImpl(userRepo, passwordEncoder, jwtUtils, entityDtoMapper);
    }

    @ParameterizedTest
    @MethodSource("provideUserRoles")
    void registerUserWithDifferentRoles(String inputRole, UserRole expectedRole) {
        // Arrange
        userDto.setRole(inputRole);
        user.setRole(expectedRole);

        // Stub the mocks
        when(passwordEncoder.encode("12345")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(entityDtoMapper.mapUserToDtoBasic(user)).thenReturn(userDto);

        // Act
        Response response = userService.registerUser(userDto);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("User successfully added", response.getMessage()),
                () -> assertEquals(expectedRole, user.getRole())
        );
    }

    @Test
    void userEmailNotFound() {
        // Arrange
        String invalidEmail = "y@gmail.com";
        when(userRepo.findByEmail(invalidEmail)).thenThrow(new NotFoundException("Email not found"));

        // Act
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(invalidEmail);

        // Assert
        assertThrows(NotFoundException.class, () -> userService.loginUser(loginRequest));
    }

    @Test
    void passwordNotMatch() {
        // Arrange
        String incorrectPassword = "wrongPassword";
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(incorrectPassword, user.getPassword())).thenReturn(false);

        // Act
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(incorrectPassword);

        // Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(loginRequest));
    }

    @Test
    void loginUserWithCorrectEmailAndPassword() {
        // Arrange
        String correctPassword = "4321";
        String encodedPassword = passwordEncoder.encode(correctPassword);
        user.setPassword(encodedPassword);  // Set the encoded password in the user entity

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(correctPassword, encodedPassword)).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("mocked-jwt-token");

        // Act
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(correctPassword);

        Response response = userService.loginUser(loginRequest);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("User log in successfully", response.getMessage()),
                () -> assertEquals("USER", response.getRole())
        );
    }

    @Test
    void getAllUserTest() {
        // Arrange
        List<User> users = List.of(user);
        when(userRepo.findAll()).thenReturn(users);
        when(entityDtoMapper.mapUserToDtoBasic(user)).thenReturn(userDto);

        // Act
        Response response = userService.getAllUser(userDto);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("Successful", response.getMessage()),
                () -> assertEquals(1, response.getUserList().size())
        );
    }

    static Stream<Arguments> provideUserRoles() {
        return Stream.of(
                Arguments.of("user", UserRole.USER),
                Arguments.of("admin", UserRole.ADMIN),
                Arguments.of(null, UserRole.USER)
        );
    }
}
