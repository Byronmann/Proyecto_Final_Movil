package com.example.pfmovil.model

data class RegisterRequest(
    val username: String,
    val password: String,
    val full_name: String,
    val age: Int,
    val email: String,
    val profile_image_base64: String? = null
)
