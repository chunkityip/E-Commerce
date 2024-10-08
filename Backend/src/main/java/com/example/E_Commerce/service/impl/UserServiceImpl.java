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
import com.example.E_Commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {
        UserRole role = UserRole.USER;

        // Situation for an admin role, not user
        if (registrationRequest.getRole() != null &&
                registrationRequest.getRole().equalsIgnoreCase("admin")) {
            role = UserRole.ADMIN;
        }

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .password(registrationRequest.getPassword())
                .role(role)
                .build();

        User saveUser = userRepo.save(user);

        UserDto userDto = entityDtoMapper.mapUserToDtoBasic(saveUser);

        return Response.builder()
                .status(200)
                .message("User successfully added")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {

        User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new NotFoundException("Email not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Password does not match");
        }
        String token = jwtUtils.generateToken(user);

        return Response.builder()
                .status(200)
                .message("User log in successfully")
                .token(token)
                .expirationTime("6 Month")
                .role(user.getRole().name())
                .build();
    }


    @Override
    public Response getAllUser(UserDto registrationRequest) {
        List<User> users = userRepo.findAll();
        List<UserDto> userDto = users.stream()
                .map(entityDtoMapper::mapUserToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .message("Successful")
                .userList(userDto)
                .build();
    }

    @Override
    public User getLoginUser() {
        return null;
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        return null;
    }
}
