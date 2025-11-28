package com.example.pfmovil.model

data class ProfileResponse(
    val id: Int,
    val username: String,
    val full_name: String,
    val age: Int,
    val email: String,
    val profile_image_base64: String?,
    val created_at: String
)
