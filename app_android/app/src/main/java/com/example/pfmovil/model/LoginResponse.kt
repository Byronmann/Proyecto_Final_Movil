package com.example.pfmovil.model

data class LoginResponse(
    val user: UserLogin,
    val token: String
)

data class UserLogin(
    val id: Int,
    val username: String
)
