package com.example.pfmovil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pfmovil.databinding.ActivityProfileBinding
import com.example.pfmovil.model.ProfileResponse
import com.example.pfmovil.model.UpdateProfileRequest
import com.example.pfmovil.network.ApiClient
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var token: String? = null
    private var userId: Int = -1

    // Guardaremos aquí siempre la imagen actual en base64
    private var currentImageBase64: String? = null

    // Launcher para tomar foto (devuelve Bitmap)
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                binding.imgProfile.setImageBitmap(bitmap)
                currentImageBase64 = encodeBitmapToBase64(bitmap)
            } else {
                Toast.makeText(this, "No se tomó la foto", Toast.LENGTH_SHORT).show()
            }
        }

    // Launcher para elegir imagen de galería
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                if (bitmap != null) {
                    binding.imgProfile.setImageBitmap(bitmap)
                    currentImageBase64 = encodeBitmapToBase64(bitmap)
                } else {
                    Toast.makeText(this, "No se pudo leer la imagen", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Leer token e id de SharedPreferences
        val prefs = getSharedPreferences("pfmovil_prefs", MODE_PRIVATE)
        token = prefs.getString("token", null)
        userId = prefs.getInt("user_id", -1)

        if (token == null || userId == -1) {
            Toast.makeText(this, "Sesión no válida. Vuelva a iniciar sesión.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Cargar datos del perfil desde backend
        loadProfile()

        // 3. Botón guardar cambios
        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }

        // 4. Botón tomar foto
        binding.btnTakePhoto.setOnClickListener {
            takePictureLauncher.launch(null)
        }

        // 5. Botón elegir de galería
        binding.btnPickFromGallery.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    // ==================== CARGAR PERFIL ====================

    private fun loadProfile() {
        val tkn = token ?: return
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getProfile("Bearer $tkn", userId)
                if (response.isSuccessful) {
                    val body: ProfileResponse? = response.body()
                    if (body != null) {
                        binding.edtProfileName.setText(body.full_name)
                        binding.edtProfileAge.setText(body.age.toString())
                        binding.edtProfileEmail.setText(body.email)

                        // Guardamos la imagen inicial
                        currentImageBase64 = body.profile_image_base64

                        // Si hay imagen, la mostramos
                        body.profile_image_base64?.let { base64 ->
                            val bmp = decodeBase64ToBitmap(base64)
                            if (bmp != null) {
                                binding.imgProfile.setImageBitmap(bmp)
                            }
                        }
                    } else {
                        Toast.makeText(this@ProfileActivity, "Perfil vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Error al cargar perfil: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // ==================== GUARDAR PERFIL ====================

    private fun saveProfile() {
        val tkn = token ?: return

        val fullName = binding.edtProfileName.text.toString().trim()
        val ageText = binding.edtProfileAge.text.toString().trim()
        val email = binding.edtProfileEmail.text.toString().trim()

        if (fullName.isEmpty() || ageText.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Complete nombre, edad y correo", Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageText.toIntOrNull()
        if (age == null) {
            Toast.makeText(this, "Edad no válida", Toast.LENGTH_SHORT).show()
            return
        }

        val request = UpdateProfileRequest(
            full_name = fullName,
            age = age,
            email = email,
            profile_image_base64 = currentImageBase64 // puede ser la misma o una nueva
        )

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.updateProfile("Bearer $tkn", userId, request)
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Error al guardar: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // ==================== HELPERS IMAGEN ====================

    private fun decodeBase64ToBitmap(base64: String): Bitmap? {
        return try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        // Comprimir a JPG calidad 80 para no hacerla gigante
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
