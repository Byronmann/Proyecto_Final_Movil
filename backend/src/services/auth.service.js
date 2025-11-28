const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const UsersRepository = require('../repositories/users.repository');
require('dotenv').config();

const SALT_ROUNDS = 10;

const AuthService = {
  async register({ username, password, full_name, age, email, profile_image_base64 }) {
    // verificar si usuario existe
    const existing = await UsersRepository.findByUsername(username);
    if (existing) throw new Error('Usuario ya existe');

    // hashear contraseña
    const password_hash = await bcrypt.hash(password, SALT_ROUNDS);

    // convertir imagen base64 a Buffer (si se envió)
    let profile_image = null;
    if (profile_image_base64) {
      profile_image = Buffer.from(profile_image_base64, 'base64');
    }

    const user = await UsersRepository.create({
      username,
      password_hash,
      full_name,
      age,
      email,
      profile_image
    });

    // generar token
    const token = jwt.sign({ userId: user.id, username: user.username }, process.env.JWT_SECRET, { expiresIn: '7d' });

    return { user, token };
  },

  async login({ username, password }) {
    const user = await UsersRepository.findByUsername(username);
    if (!user) throw new Error('Usuario o contraseña incorrectos');

    const match = await bcrypt.compare(password, user.password_hash);
    if (!match) throw new Error('Usuario o contraseña incorrectos');

    const token = jwt.sign({ userId: user.id, username: user.username }, process.env.JWT_SECRET, { expiresIn: '7d' });

    return { user: { id: user.id, username: user.username }, token };
  }
};

module.exports = AuthService;
