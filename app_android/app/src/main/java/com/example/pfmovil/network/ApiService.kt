package com.example.pfmovil.network

import com.example.pfmovil.model.*
import retrofit2.Response
import retrofit2.http.*
import com.example.pfmovil.model.UpdateProfileRequest
import com.example.pfmovil.model.ProfileResponse
import com.example.pfmovil.model.RegisterRequest
import com.example.pfmovil.model.LoginResponse



interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<LoginResponse> // tu backend devuelve user+token; luego lo ajustamos si quieres

    @GET("users/{id}")
    suspend fun getProfile(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int
    ): Response<ProfileResponse>

    @PUT("users/{id}/profile")
    suspend fun updateProfile(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Body body: UpdateProfileRequest    // luego hacemos uno específico si quieres
    ): Response<ProfileResponse>
}
