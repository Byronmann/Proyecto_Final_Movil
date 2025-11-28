const express = require('express');
const router = express.Router();
const UsersController = require('../controllers/users.controller');
const authMiddleware = require('../middleware/auth.middleware');

router.get('/:id', authMiddleware, UsersController.getProfile);
router.put('/:id/profile', authMiddleware, UsersController.updateProfile);

module.exports = router;
