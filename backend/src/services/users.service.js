const UsersRepository = require('../repositories/users.repository');

const UsersService = {
  async getProfile(id) {
    const user = await UsersRepository.findById(id);
    if (!user) throw new Error('Usuario no encontrado');

    // convertir image Buffer -> base64 (si existe)
    let profile_image_base64 = null;
    if (user.profile_image) {
      profile_image_base64 = user.profile_image.toString('base64');
    }

    return {
      id: user.id,
      username: user.username,
      full_name: user.full_name,
      age: user.age,
      email: user.email,
      profile_image_base64,
      created_at: user.created_at
    };
  },

  async updateProfile(id, { full_name, age, email, profile_image_base64 }) {
    let profile_image = null;
    if (profile_image_base64) profile_image = Buffer.from(profile_image_base64, 'base64');

    const updated = await UsersRepository.updateProfile(id, { full_name, age, email, profile_image });
    if (!updated) throw new Error('No se pudo actualizar perfil');

    let profile_image_b64 = null;
    if (updated.profile_image) profile_image_b64 = updated.profile_image.toString('base64');

    return {
      id: updated.id,
      username: updated.username,
      full_name: updated.full_name,
      age: updated.age,
      email: updated.email,
      profile_image_base64: profile_image_b64,
      created_at: updated.created_at
    };
  }
};

module.exports = UsersService;
