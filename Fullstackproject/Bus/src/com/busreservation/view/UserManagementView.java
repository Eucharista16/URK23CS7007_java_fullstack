package com.busreservation.view;

import com.busreservation.controller.UserController;
import com.busreservation.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementView extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private UserController userController;
    
    public UserManagementView() {
        userController = new UserController();
        initComponents();
        loadUsers();
    }
    
    private void initComponents() {
        setTitle("Bus Reservation System - User Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        
        // Create table model with columns
        String[] columns = {"User ID", "Username", "Full Name", "Email", "Phone", "User Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        deleteButton = new JButton("Delete User");
        refreshButton = new JButton("Refresh");
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        buttonPanel.add(addButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserDialog(null);
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedUser();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUser();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
        
        userTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = userTable.getSelectedRow() != -1;
            editButton.setEnabled(rowSelected);
            deleteButton.setEnabled(rowSelected);
        });
    }
    
    private void loadUsers() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get all users
        List<User> users = userController.getAllUsers();
        
        // Add users to table
        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getUserType()
            };
            tableModel.addRow(row);
        }
    }
    
    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        User user = userController.getUserById(userId);
        
        if (user != null) {
            showUserDialog(user);
        }
    }
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Confirm deletion
        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this user?", 
                "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            boolean success = userController.deleteUser(userId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "User deleted successfully", 
                                             "Delete Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user. User may have reservations.", 
                                             "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showUserDialog(User user) {
        // Create a dialog for adding/editing user
        JDialog dialog = new JDialog(this, user == null ? "Add New User" : "Edit User", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        
        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField fullNameField = new JTextField(20);
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(20);
        
        JLabel userTypeLabel = new JLabel("User Type:");
        JComboBox<User.UserType> userTypeCombo = new JComboBox<>(User.UserType.values());
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        // If editing, populate fields with user data
        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            fullNameField.setText(user.getFullName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhone());
            userTypeCombo.setSelectedItem(user.getUserType());
        }
        
        // Add components to panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(fullNameLabel);
        panel.add(fullNameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(userTypeLabel);
        panel.add(userTypeCombo);
        panel.add(saveButton);
        panel.add(cancelButton);
        
        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate input
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                User.UserType userType = (User.UserType) userTypeCombo.getSelectedItem();
                
                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields except phone are required", 
                                                 "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create or update user object
                User userToBeSaved;
                if (user == null) {
                    userToBeSaved = new User();
                } else {
                    userToBeSaved = user;
                }
                
                userToBeSaved.setUsername(username);
                userToBeSaved.setPassword(password);
                userToBeSaved.setFullName(fullName);
                userToBeSaved.setEmail(email);
                userToBeSaved.setPhone(phone);
                userToBeSaved.setUserType(userType);
                
                boolean success;
                if (user == null) {
                    success = userController.register(userToBeSaved);
                } else {
                    success = userController.updateUser(userToBeSaved);
                }
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "User saved successfully", 
                                                 "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUsers(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to save user", 
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}

