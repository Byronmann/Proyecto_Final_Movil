const AuthService = require('../services/auth.service');

const AuthController = {
  async register(req, res) {
    try {
      const { username, password, full_name, age, email, profile_image_base64 } = req.body;
      if (!username || !password || !full_name || !age || !email) {
        return res.status(400).json({ message: 'Faltan datos obligatorios' });
      }

      const result = await AuthService.register({ username, password, full_name, age, email, profile_image_base64 });
      return res.status(201).json(result);
    } catch (err) {
      return res.status(400).json({ message: err.message || 'Error en registro' });
    }
  },

  async login(req, res) {
    try {
      const { username, password } = req.body;
      if (!username || !password) return res.status(400).json({ message: 'Faltan credenciales' });

      const result = await AuthService.login({ username, password });
      return res.json(result);
    } catch (err) {
      return res.status(401).json({ message: err.message || 'Error en login' });
    }
  }
};

module.exports = AuthController;
