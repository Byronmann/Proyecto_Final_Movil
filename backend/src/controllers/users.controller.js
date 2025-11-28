const UsersService = require('../services/users.service');

const UsersController = {
  async getProfile(req, res) {
    try {
      const id = parseInt(req.params.id, 10);
      // el middleware ya asegura que req.user existe
      if (req.user.userId !== id) return res.status(403).json({ message: 'No autorizado' });

      const data = await UsersService.getProfile(id);
      return res.json(data);
    } catch (err) {
      return res.status(404).json({ message: err.message || 'No encontrado' });
    }
  },

  async updateProfile(req, res) {
    try {
      const id = parseInt(req.params.id, 10);
      if (req.user.userId !== id) return res.status(403).json({ message: 'No autorizado' });

      const { full_name, age, email, profile_image_base64 } = req.body;
      // validaciones simples
      if (!full_name || !age || !email) return res.status(400).json({ message: 'Faltan datos' });

      const updated = await UsersService.updateProfile(id, { full_name, age, email, profile_image_base64 });
      return res.json(updated);
    } catch (err) {
      return res.status(400).json({ message: err.message || 'Error al actualizar' });
    }
  }
};

module.exports = UsersController;
