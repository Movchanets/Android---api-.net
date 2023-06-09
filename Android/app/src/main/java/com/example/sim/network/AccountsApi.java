package com.example.sim.network;

import com.example.sim.dto.account.EditAccountDTO;
import com.example.sim.dto.account.LoginDTO;
import com.example.sim.dto.account.LoginResponse;
import com.example.sim.dto.account.RegisterDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AccountsApi {
@POST("/api/account/register")
public Call<LoginResponse> register(@Body RegisterDTO model);
@POST("/api/account/login")
public Call<LoginResponse> login(@Body LoginDTO model);

@PUT("/api/account/update")
public Call<LoginResponse> update(@Body EditAccountDTO model);
}
