package com.example.E_Commerce.service.impl;

import com.example.E_Commerce.dto.AddressDto;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.entity.Address;
import com.example.E_Commerce.entity.User;
import com.example.E_Commerce.repository.AddressRepo;
import com.example.E_Commerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserService userService;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User user;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        // Mock SecurityContext and Authentication
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);

        // Mock the authenticated user's email
//        when(authentication.getName()).thenReturn("test@example.com");

        // Create and configure the user
        user = new User();
        user.setEmail("test@example.com");
        when(userService.getLoginUser()).thenReturn(user);
    }

    @Test
    void saveAddressTest() {
        // Create AddressDto with test data
        addressDto = new AddressDto();
        addressDto.setStreet("123 Main St");
        addressDto.setCity("CityVille");
        addressDto.setState("CA");
        addressDto.setZipCode("90001");
        addressDto.setCountry("USA");

        user.setAddress(null); // Ensure the user has no address initially

        // Act
        Response response = addressService.saveAndUpdateAddress(addressDto);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("Address successfully created", response.getMessage())
        );

        verify(addressRepo, times(1)).save(any(Address.class));
    }

    @Test
    void updateAddressTest() {
        // Arrange
        addressDto = new AddressDto();
        addressDto.setStreet("1961 79th St");

        // Create existing address for the user
        Address existAddress = new Address();
        existAddress.setStreet("Old St");
        user.setAddress(existAddress); // Set the existing address for the user

        // Stub
        when(userService.getLoginUser()).thenReturn(user);

        // Act
        Response response = addressService.saveAndUpdateAddress(addressDto);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus(), "Status should match"),
                () -> assertEquals("Address successfully updated", response.getMessage(), "Message should match")
        );

        // Verify that save method was called and the street is updated
        verify(addressRepo, times(1)).save(any(Address.class));
        assertEquals("1961 79th St", existAddress.getStreet(), "Address should match");
    }
}
