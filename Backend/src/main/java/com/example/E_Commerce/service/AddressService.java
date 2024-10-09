package com.example.E_Commerce.service;

import com.example.E_Commerce.dto.AddressDto;
import com.example.E_Commerce.dto.Response;

public interface AddressService {

    Response saveAndUpdateAddress(AddressDto addressDto);
}
