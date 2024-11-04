package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.dto.LoginRequest;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.dto.UserDto;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.enums.UserRole;
import com.example.E_Commerce.exception.InvalidCredentialsException;
import com.example.E_Commerce.mapper.EntityDtoMapper;
import com.example.E_Commerce.repository.UserRepo;
import com.example.E_Commerce.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    private UserDto userDto;
    private User exceptedOutput;

    @BeforeEach
    void setUp() {
        // Initialize UserDto
        userDto = new UserDto();
        userDto.setName("CK");
        userDto.setEmail("yck11214@gmail.com");
        userDto.setPassword("12345");

        // Initialize User entity
      exceptedOutput = User.builder()
                .name("CK")
                .email("yck11214@gmail.com")
                .password("passwordEncoder")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void registerUserTest() {
      // Arrange
      userDto.setRole("user");
      when(passwordEncoder.encode(userDto.getPassword())).thenReturn("passwordEncoder");
      when(userRepo.save(any(User.class))).thenReturn(exceptedOutput);
      when(entityDtoMapper.mapUserToDtoBasic(exceptedOutput)).thenReturn(userDto);

      // Act
      Response response = userService.registerUser(userDto);

      // Assert
      assertAll(
        () -> assertEquals(200 , response.getStatus() , "The status should match"),
        () -> assertEquals("User successfully added", response.getMessage() , "The message should match")
      );

      verify(passwordEncoder).encode(userDto.getPassword());
      verify(userRepo).save(any(User.class));
      verify(entityDtoMapper).mapUserToDtoBasic(exceptedOutput);

      ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
      verify(userRepo).save(userCaptor.capture());

      User captueredUser = userCaptor.getValue();

      assertAll(
        () ->  assertEquals("CK", captueredUser.getName(), "User name should be CK"),
        () -> assertEquals("yck11214@gmail.com" , captueredUser.getEmail(), "Email should match")
      );

    }








//    @Test
//    void passwordNotMatch() {
//        // Arrange
//        String incorrectPassword = "wrongPassword";
//        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(incorrectPassword, user.getPassword())).thenReturn(false);
//
//        // Act
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail(user.getEmail());
//        loginRequest.setPassword(incorrectPassword);
//
//        // Assert
//        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(loginRequest));
//    }
//
//    @Test
//    void loginUserWithCorrectEmailAndPassword() {
//        // Arrange
//        String correctPassword = "4321";
//        String encodedPassword = passwordEncoder.encode(correctPassword);
//        user.setPassword(encodedPassword);  // Set the encoded password in the user entity
//
//        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(correctPassword, encodedPassword)).thenReturn(true);
//        when(jwtUtils.generateToken(user)).thenReturn("mocked-jwt-token");
//
//        // Act
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail(user.getEmail());
//        loginRequest.setPassword(correctPassword);
//
//        Response response = userService.loginUser(loginRequest);
//
//        // Assert
//        assertAll(
//                () -> assertEquals(200, response.getStatus() , "Status should match as 200"),
//                () -> assertEquals("User log in successfully", response.getMessage() , "Message should display as Successful"),
//                () -> assertEquals("USER", response.getRole(), "Role should match as USER")
//        );
//    }
//
//
//
//    static Stream<Arguments> provideUserRoles() {
//        return Stream.of(
//                Arguments.of("user", UserRole.USER),
//                Arguments.of("admin", UserRole.ADMIN),
//                Arguments.of(null, UserRole.USER)
//        );
//    }
//
//    @Test
//    void getLoginUserNameNotFound() {
//        String email = "unknown@example.com";
//
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        when(authentication.getName()).thenReturn(email);
//
//        when(userRepo.findByEmail(any()))
//                .thenThrow(new UsernameNotFoundException("User not found"));
//
//        assertThrows(UsernameNotFoundException.class , () ->
//                userService.getLoginUser());
//    }
//
//    @Test
//    void getLoginUserWithCorrectEmail() {
//        String email = "yck11214@gmail.com";
//
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        when(authentication.getName()).thenReturn(email);
//
//        when(userRepo.findByEmail(email))
//                .thenReturn(Optional.of(user));
//
//        User result = userService.getLoginUser();
//
//        assertAll(
//                () -> assertEquals("CK", result.getName(), "User name should match"),
//                () -> assertEquals(email, result.getEmail(), "User email should match"),
//                () -> assertEquals(UserRole.USER, result.getRole(), "User role should match")  // Add role assertion if applicable
//        );
//    }
//
//    @Test
//    void getUserInfoAndOrderHistoryTest() {
//        // Arrange
//        // Stub
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//
//        SecurityContextHolder.setContext(securityContext);
//
//        when(authentication.getName()).thenReturn(user.getEmail());
//
//        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        when(entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user)).thenReturn(userDto);
//
//        // Act
//        Response response = userService.getUserInfoAndOrderHistory();
//
//        // Assert
//        assertAll(
//                () -> assertEquals(200, response.getStatus(), "Status should match as 200"),
//                () -> assertEquals(userDto, response.getUser())
//        );
//
//        // Verify
//        verify(userRepo , times(1)).findByEmail(user.getEmail());
//        verify(entityDtoMapper, times(1)).mapUserToDtoPlusAddressAndOrderHistory(user);
//    }

}
