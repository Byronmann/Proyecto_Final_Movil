package com.example.pfmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pfmovil.databinding.ActivityLoginBinding
import com.example.pfmovil.model.LoginRequest
import com.example.pfmovil.network.ApiClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ir a registro
        binding.txtGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Botón Iniciar sesión
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                doLogin(username, password)
            }
        }
    }

    private fun doLogin(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.login(LoginRequest(username, password))

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Guardar token e id en SharedPreferences
                        val prefs = getSharedPreferences("pfmovil_prefs", MODE_PRIVATE)
                        prefs.edit()
                            .putString("token", body.token)
                            .putInt("user_id", body.user.id)
                            .apply()

                        Toast.makeText(this@LoginActivity, "Login correcto", Toast.LENGTH_SHORT).show()

                        // Ir al perfil
                        val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                        startActivity(intent)
                        finish() // para no volver al login con back
                    } else {
                        Toast.makeText(this@LoginActivity, "Respuesta vacía del servidor", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
