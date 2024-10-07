package com.example.E_Commerce.service;

import com.example.E_Commerce.dto.LoginRequest;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.dto.UserDto;
import com.example.E_Commerce.entity.User;


public interface UserService {

    Response registerUser(UserDto registrationRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUser(UserDto registrationRequest);

    User getLoginUser();

    Response getUserInfoAndOrderHistory();


}
