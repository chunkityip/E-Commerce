package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.dto.UserDto;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.enums.UserRole;
import com.example.E_Commerce.mapper.EntityDtoMapper;
import com.example.E_Commerce.repository.UserRepo;
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

    //Inject
    @InjectMocks
    private UserServiceImpl userService;

    // Method to have different user role (null , admin and user0
    static Stream<Arguments> provideUserRoles() {
        return Stream.of(
                Arguments.of("user" , UserRole.USER),
                Arguments.of("admin" , UserRole.ADMIN),
                Arguments.of(null , UserRole.USER)
        );
    }


    @ParameterizedTest
    @MethodSource("provideUserRoles")
    void registerUserWithDifferentRoles(String inputRole , UserRole expectedRole) {
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
                () -> assertEquals(200 , response.getStatus()),
                () -> assertEquals("User successfully added" , response.getMessage()),
                () -> assertEquals(expectedRole, user.getRole())
                );
    }

}