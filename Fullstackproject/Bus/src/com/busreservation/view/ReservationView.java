package com.busreservation.view;

import com.busreservation.controller.ReservationController;
import com.busreservation.model.Reservation;
import com.busreservation.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReservationView extends JFrame {
    private User currentUser;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private JButton cancelButton;
    private JButton refreshButton;
    private ReservationController reservationController;
    
    public ReservationView(User user) {
        this.currentUser = user;
        reservationController = new ReservationController();
        initComponents();
        loadReservations();
    }
    
    private void initComponents() {
        setTitle("Bus Reservation System - My Reservations");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("My Reservations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        
        // Create table model with columns
        String[] columns = {"Reservation ID", "Bus Number", "Bus Name", "From", "To", "Departure", "Journey Date", "Seats", "Total Fare", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        reservationTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        refreshButton = new JButton("Refresh");
        cancelButton = new JButton("Cancel Selected Reservation");
        cancelButton.setEnabled(false);
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReservations();
            }
        });
        
        reservationTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow != -1) {
                String status = (String) tableModel.getValueAt(selectedRow, 9);
                // Only allow cancellation of confirmed reservations
                cancelButton.setEnabled("CONFIRMED".equals(status));
            } else {
                cancelButton.setEnabled(false);
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelSelectedReservation();
            }
        });
    }
    
    private void loadReservations() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get reservations for current user
        List<Reservation> reservations = reservationController.getReservationsByUserId(currentUser.getUserId());
        
        // Add reservations to table
        for (Reservation reservation : reservations) {
            Object[] row = {
                reservation.getReservationId(),
                reservation.getBus().getBusNumber(),
                reservation.getBus().getBusName(),
                reservation.getBus().getSourceLocation(),
                reservation.getBus().getDestination(),
                reservation.getBus().getDepartureTime(),
                reservation.getJourneyDate(),
                reservation.getNumberOfSeats(),
                reservation.getTotalFare(),
                reservation.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void cancelSelectedReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Confirm cancellation
        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel this reservation?", 
                "Cancel Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            boolean success = reservationController.cancelReservation(reservationId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully", 
                                             "Cancellation Success", JOptionPane.INFORMATION_MESSAGE);
                loadReservations(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel reservation", 
                                             "Cancellation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
