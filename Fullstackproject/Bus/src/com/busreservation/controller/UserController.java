package com.busreservation.controller;

import com.busreservation.dao.UserDAO;
import com.busreservation.model.User;

import java.util.List;

public class UserController {
    private UserDAO userDAO;
    
    public UserController() {
        this.userDAO = new UserDAO();
    }
    
    public User login(String username, String password) {
        return userDAO.authenticate(username, password);
    }
    
    public boolean register(User user) {
        return userDAO.registerUser(user);
    }
    
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }
    
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}
