package com.example.mobileapi.service;

import com.example.mobileapi.dto.request.IntrospectRequest;
import com.example.mobileapi.dto.request.LoginRequest;
import com.example.mobileapi.dto.response.IntrospectResponse;
import com.example.mobileapi.dto.response.LoginResponse;
import com.example.mobileapi.exception.AppException;
import com.example.mobileapi.repository.CustomerRepository;

public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest) throws AppException;

    void logout(String auth);

    IntrospectResponse introspect(IntrospectRequest request) throws Exception;


}
