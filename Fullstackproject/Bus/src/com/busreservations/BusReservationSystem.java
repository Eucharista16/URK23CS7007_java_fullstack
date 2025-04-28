package com.busreservations;

import com.busreservation.view.LoginView;
import com.busreservation.util.DatabaseConnection;

import javax.swing.*;

public class BusReservationSystem {
    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Initialize database connection
        DatabaseConnection.getConnection();
        
        // Start the application with login view
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView().setVisible(true);
            }
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                DatabaseConnection.closeConnection();
            }
        });
    }
}
