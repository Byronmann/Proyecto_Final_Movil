const pool = require('../database/connection');

const UsersRepository = {
  async create(user) {
    const { username, password_hash, full_name, age, email, profile_image } = user;
    const query = `
      INSERT INTO users (username, password_hash, full_name, age, email, profile_image)
      VALUES ($1, $2, $3, $4, $5, $6)
      RETURNING id, username, full_name, age, email, created_at;
    `;
    const values = [username, password_hash, full_name, age, email, profile_image];
    const { rows } = await pool.query(query, values);
    return rows[0];
  },

  async findByUsername(username) {
    const query = `SELECT * FROM users WHERE username = $1;`;
    const { rows } = await pool.query(query, [username]);
    return rows[0] || null;
  },

  async findById(id) {
    const query = `SELECT id, username, full_name, age, email, profile_image, created_at FROM users WHERE id = $1;`;
    const { rows } = await pool.query(query, [id]);
    return rows[0] || null;
  },

  async updateProfile(id, { full_name, age, email, profile_image }) {
    const query = `
      UPDATE users
      SET full_name = $1,
          age = $2,
          email = $3,
          profile_image = $4
      WHERE id = $5
      RETURNING id, username, full_name, age, email, profile_image, created_at;
    `;
    const values = [full_name, age, email, profile_image, id];
    const { rows } = await pool.query(query, values);
    return rows[0] || null;
  }
};

module.exports = UsersRepository;
