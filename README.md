# 📱 Proyecto Final – Desarrollo Móvil  
## Aplicación Android + Backend Node.js + PostgreSQL

### **Autor:**  
**Byron Rodolfo Maldonado Palacios – 202300076**

### **Curso:** Desarrollo Móvil  
### **Docente:** Ing. R. Arizandieta  
### **Universidad Da Vinci de Guatemala**

---

# 📝 1. Introducción

El presente proyecto consiste en el desarrollo de una aplicación móvil Android que permite a los usuarios registrarse, iniciar sesión, visualizar y actualizar su perfil, así como tomar fotografías o seleccionarlas desde la galería para utilizarlas como imagen de perfil.  

El backend fue implementado en Node.js utilizando Express y PostgreSQL, aplicando un patrón arquitectónico por capas (controladores, servicios, repositorios), asegurando un desarrollo ordenado y escalable. La comunicación entre la aplicación móvil y el backend se realiza mediante solicitudes HTTP utilizando JSON como formato de intercambio.  

El proyecto integra correctamente captura de imágenes, autenticación JWT, manejo de Base64 y almacenamiento de imágenes en formato BYTEA dentro de PostgreSQL.

---

# 🎯 2. Objetivos del Proyecto

### **Objetivo General**
Desarrollar una aplicación móvil completa que implemente registro, inicio de sesión, visualización y edición de perfil, incluyendo almacenamiento de imágenes, utilizando Android, Node.js y PostgreSQL.

### **Objetivos Específicos**
- Construir una aplicación Android con pantallas de Login, Registro y Perfil.
- Implementar autenticación basada en JWT.
- Desarrollar un backend modular con patrón arquitectónico por capas.
- Consumir endpoints REST mediante Retrofit.
- Permitir captura o selección de fotografías dentro de la app.
- Convertir imágenes a Base64 y almacenarlas como BYTEA.
- Manejar la persistencia de sesión mediante SharedPreferences.

---

# 🏗️ 3. Arquitectura del Sistema

El sistema se compone de tres capas principales:

## **3.1 Capa de Presentación (Android)**  
Responsable de la interacción con el usuario:
- Pantallas XML (Login, Registro, Perfil)
- Lógica en Kotlin
- Retrofit para llamadas HTTP
- Conversión Base64 ↔ Bitmap
- ActivityResult API para cámara y galería
- SharedPreferences para almacenar token e ID

---

## **3.2 Capa Lógica y Servicios (Backend Node.js)**  
Estructura por capas:

```
src/
 ├── controllers/
 ├── services/
 ├── repositories/
 ├── routes/
 ├── database/
 └── app.js
```

Funciones clave:
- Manejo de autenticación JWT.
- Validación de credenciales.
- Cifrado de contraseñas con bcrypt.
- Conversión Base64 → BYTEA.
- Exposición de endpoints REST.

---

## **3.3 Capa de Datos (PostgreSQL)**  

### **Tabla principal: `users`**
| Campo           | Tipo         |
|-----------------|--------------|
| id              | SERIAL (PK)  |
| username        | VARCHAR(50)  |
| password_hash   | VARCHAR(255) |
| full_name       | VARCHAR(100) |
| age             | INT          |
| email           | VARCHAR(100) |
| profile_image   | BYTEA        |
| created_at      | TIMESTAMP    |

---

# 🧩 4. Modelo de Datos

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    age INT,
    email VARCHAR(100),
    profile_image BYTEA,
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

# 🌐 5. Endpoints del Backend

## **5.1 Autenticación**
### **POST /auth/register**
Registra un usuario nuevo.  
Body:
```json
{
  "username": "user",
  "password": "123456",
  "full_name": "Nombre",
  "age": 25,
  "email": "mail@mail.com",
  "profile_image_base64": null
}
```

### **POST /auth/login**
Respuesta:
```json
{
  "user": { "id": 1, "username": "user" },
  "token": "jwt_token"
}
```

---

## **5.2 Perfil**
### **GET /users/:id**
Retorna datos del usuario + foto Base64.

### **PUT /users/:id/profile**
Actualiza información y la imagen.

Body:
```json
{
  "full_name": "Nombre",
  "age": 25,
  "email": "correo@mail.com",
  "profile_image_base64": "..."
}
```

---

# 📱 6. Flujo de la Aplicación

1. **Registro**  
2. **Inicio de sesión**  
3. **Carga de perfil**  
4. **Actualización de perfil**  
5. **Almacenamiento en PostgreSQL**

---

# 📸 7. Capturas sugeridas

(Agregar en el documento final)
- Registro
- Login
- Perfil cargado
- Toma de foto
- Galería
- Verificación en BD
- Estructura del backend

---

# 📝 8. Conclusiones

- La app integra Android, Node.js y PostgreSQL de forma exitosa.  
- Se implementó autenticación JWT y almacenamiento de imágenes.  
- Se aplicaron patrones modernos de programación móvil y backend.  
- Se cumplieron todos los requisitos del proyecto.  

---

# 👤 9. Autor

**Byron Rodolfo Maldonado Palacios (202300076)**

---

# 👥 10. Colaborador requerido

```
rarizandieta
```

---
