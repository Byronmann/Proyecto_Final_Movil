package com.example.pfmovil

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pfmovil.databinding.ActivityRegisterBinding
import com.example.pfmovil.model.RegisterRequest
import com.example.pfmovil.network.ApiClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister() {
        val username = binding.edtRegUsername.text.toString().trim()
        val password = binding.edtRegPassword.text.toString().trim()
        val fullName = binding.edtRegFullName.text.toString().trim()
        val ageText = binding.edtRegAge.text.toString().trim()
        val email = binding.edtRegEmail.text.toString().trim()

        if (username.isEmpty() || password.isEmpty() ||
            fullName.isEmpty() || ageText.isEmpty() || email.isEmpty()
        ) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageText.toIntOrNull()
        if (age == null) {
            Toast.makeText(this, "Edad no válida", Toast.LENGTH_SHORT).show()
            return
        }

        val request = RegisterRequest(
            username = username,
            password = password,
            full_name = fullName,
            age = age,
            email = email,
            profile_image_base64 = null   // se puede agregar luego si quieres
        )

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.register(request)
                if (response.isSuccessful) {
                    // Podríamos guardar token e ir directo al perfil, pero para el flujo del proyecto
                    // lo dejamos simple: vuelve al login.
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registro exitoso. Ahora puede iniciar sesión.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish() // regresa a LoginActivity
                } else {
                    // Si el usuario ya existe, tu backend manda 400 con mensaje "Usuario ya existe"
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error al registrar: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@RegisterActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
