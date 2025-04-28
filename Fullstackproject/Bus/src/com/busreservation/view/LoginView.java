package com.busreservation.view;

import com.busreservation.controller.UserController;
import com.busreservation.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserController userController;
    
    public LoginView() {
        userController = new UserController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Bus Reservation System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                
                // Title Panel
                JPanel titlePanel = new JPanel();
                JLabel titleLabel = new JLabel("Bus Reservation System");
                titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
                titlePanel.add(titleLabel);
                
                // Form Panel
                JPanel formPanel = new JPanel();
                formPanel.setLayout(new GridLayout(2, 2, 10, 10));
                
                JLabel usernameLabel = new JLabel("Username:");
                usernameField = new JTextField(20);
                JLabel passwordLabel = new JLabel("Password:");
                passwordField = new JPasswordField(20);
                
                formPanel.add(usernameLabel);
                formPanel.add(usernameField);
                formPanel.add(passwordLabel);
                formPanel.add(passwordField);
                
                // Button Panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
                
                loginButton = new JButton("Login");
                registerButton = new JButton("Register");
                
                buttonPanel.add(loginButton);
                buttonPanel.add(registerButton);
                
                // Add panels to main panel
                mainPanel.add(titlePanel, BorderLayout.NORTH);
                mainPanel.add(formPanel, BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                // Add main panel to frame
                add(mainPanel);
                
                // Add action listeners
                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        login();
                    }
                });
                
                registerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openRegisterView();
                    }
                });
            }
            
            private void login() {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username and password cannot be empty", 
                                                 "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User user = userController.login(username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Login successful!", 
                                                 "Success", JOptionPane.INFORMATION_MESSAGE);
                    openMainView(user);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", 
                                                 "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            private void openRegisterView() {
                // Create and show register view
                RegisterView registerView = new RegisterView(this);
                registerView.setVisible(true);
                this.setVisible(false);
            }
            
            private void openMainView(User user) {
                // Create and show main view
                MainView mainView = new MainView(user);
                mainView.setVisible(true);
                this.dispose();
            }
            
            public static void main(String[] args) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new LoginView().setVisible(true);
                    }
                });
            }
        }
          