package com.example.pfmovil.model

data class UpdateProfileRequest(
    val full_name: String,
    val age: Int,
    val email: String,
    val profile_image_base64: String? = null
)
