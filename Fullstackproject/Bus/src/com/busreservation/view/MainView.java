package com.busreservation.view;

import com.busreservation.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private User currentUser;
    private JButton searchBusButton;
    private JButton viewReservationsButton;
    private JButton manageBusesButton;
    private JButton manageUsersButton;
    private JButton logoutButton;
    
    public MainView(User user) {
        this.currentUser = user;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Bus Reservation System - Main Menu");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome Panel
        JPanel welcomePanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomePanel.add(welcomeLabel);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
        
        searchBusButton = new JButton("Search and Book Bus");
        viewReservationsButton = new JButton("View My Reservations");
        manageBusesButton = new JButton("Manage Buses");
        manageUsersButton = new JButton("Manage Users");
        logoutButton = new JButton("Logout");
        
        buttonPanel.add(searchBusButton);
        buttonPanel.add(viewReservationsButton);
        
        // Only show admin options to admin users
        if (currentUser.getUserType() == User.UserType.ADMIN) {
            buttonPanel.add(manageBusesButton);
            buttonPanel.add(manageUsersButton);
        }
        
        buttonPanel.add(logoutButton);
        
        // Add panels to main panel
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        searchBusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openBusSearchView();
            }
        });
        
        viewReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReservationsView();
            }
        });
        
        manageBusesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openBusManagementView();
            }
        });
        
        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUserManagementView();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }
    
    private void openBusSearchView() {
        BusSearchView busSearchView = new BusSearchView(currentUser);
        busSearchView.setVisible(true);
    }
    
    private void openReservationsView() {
        ReservationView reservationView = new ReservationView(currentUser);
        reservationView.setVisible(true);
    }
    
    private void openBusManagementView() {
        BusManagementView busManagementView = new BusManagementView();
        busManagementView.setVisible(true);
    }
    
    private void openUserManagementView() {
        UserManagementView userManagementView = new UserManagementView();
        userManagementView.setVisible(true);
    }
    
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", 
                                                 "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            this.dispose();
        }
    }
}
