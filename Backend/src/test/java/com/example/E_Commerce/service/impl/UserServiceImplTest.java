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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    //Mock
    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    @Mock
    private JwtUtils jwtUtils;

    //Inject
    @InjectMocks
    private UserServiceImpl userService;

    // Method to have different user role (null , admin and user)
    static Stream<Arguments> provideUserRoles() {
        return Stream.of(
                Arguments.of("user", UserRole.USER),
                Arguments.of("admin", UserRole.ADMIN),
                Arguments.of(null, UserRole.USER)
        );
    }


    @ParameterizedTest
    @MethodSource("provideUserRoles")
    void registerUserWithDifferentRoles(String inputRole, UserRole expectedRole) {
        //Arrange
        UserDto request = new UserDto();
        request.setName("CK");
        request.setEmail("yck11214@gmail.com");
        request.setPassword("12345");
        request.setRole(inputRole);

        User user = User.builder()
                .name("CK")
                .email("yck11214@gmail.com")
                .password("passwordEncoder")
                .role(expectedRole)
                .build();

        UserDto mappedUser = new UserDto();
        mappedUser.setName("CK");
        mappedUser.setEmail("yck11214@gmail.com");


        // Stub
        when(passwordEncoder.encode("12345"))
                .thenReturn("encodedPassword");

        when(userRepo.save(any(User.class))).
                thenReturn(user);

        when(entityDtoMapper.mapUserToDtoBasic(user))
                .thenReturn(mappedUser);

        // Act
        Response response = userService.registerUser(request);

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

        // Mock the repository to throw NotFoundException when the email is not found
        when(userRepo.findByEmail(invalidEmail))
                .thenThrow(new NotFoundException("Email not found"));


        // Act
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(invalidEmail);

        // Assert
        assertThrows(NotFoundException.class, () ->
                userService.loginUser(loginRequest));
    }

    @Test
    void passwordNotMatch() {
        // Arrange
        String email = "y@gmail.com";
        String inCorrectPassword = ("4321");
        String correctPassword = ("4321");
        String encodedPassword = passwordEncoder.encode(correctPassword);

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(encodedPassword))
                .role(UserRole.USER)
                .build();


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(correctPassword);


        when(userRepo.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(inCorrectPassword, user.getPassword()))
                .thenReturn(false);


        // Assert
        assertThrows(InvalidCredentialsException.class, () ->
                userService.loginUser(loginRequest));
    }

    @Test
    void loginUserWithCorrectEmailAndPassword() {
        // Arrange
        String email = "y@gmail.com";
        String correctPassword = "4321";  // This is the raw password that the user provides
        String encodedPassword = passwordEncoder.encode(correctPassword);  // Encode the correct password

        // Create a User object with the encoded password
        User user = User.builder()
                .email(email)
                .password(encodedPassword)  // Use the encoded password
                .role(UserRole.USER)
                .build();

        // Create a LoginRequest with the raw password
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(correctPassword);  // Set the raw password for comparison

        // Mock the JWT token generation
        String token = "mocked-jwt-token";
        when(jwtUtils.generateToken(user)).thenReturn(token);

        // Mock the userRepo to return the user when the email is found
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // ** Mock the passwordEncoder's matches method to return true
        when(passwordEncoder.matches(correctPassword, encodedPassword)).thenReturn(true);

        // Act
        Response response = userService.loginUser(loginRequest);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("User log in successfully", response.getMessage()),
                () -> assertEquals("USER", response.getRole())
        );
    }
}