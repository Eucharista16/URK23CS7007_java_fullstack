package com.busreservation.view;

import com.busreservation.controller.BusController;
import com.busreservation.controller.ReservationController;
import com.busreservation.model.Bus;
import com.busreservation.model.Reservation;
import com.busreservation.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BusSearchView extends JFrame {
    private User currentUser;
    private JTextField sourceField;
    private JTextField destinationField;
    private JTextField dateField;
    private JButton searchButton;
    private JTable busTable;
    private DefaultTableModel tableModel;
    private JButton bookButton;
    private JSpinner seatsSpinner;
    private BusController busController;
    private ReservationController reservationController;
    
    public BusSearchView(User user) {
        this.currentUser = user;
        busController = new BusController();
        reservationController = new ReservationController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Bus Reservation System - Search Buses");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 7, 5, 5));
        
        JLabel sourceLabel = new JLabel("From:");
        sourceField = new JTextField(15);
        JLabel destinationLabel = new JLabel("To:");
        destinationField = new JTextField(15);
        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        dateField = new JTextField(10);
        searchButton = new JButton("Search");
        
        searchPanel.add(sourceLabel);
        searchPanel.add(sourceField);
        searchPanel.add(destinationLabel);
        searchPanel.add(destinationField);
        searchPanel.add(dateLabel);
        searchPanel.add(dateField);
        searchPanel.add(searchButton);
        
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        
        // Create table model with columns
        String[] columns = {"Bus ID", "Bus Number", "Bus Name", "From", "To", "Departure", "Arrival", "Date", "Available Seats", "Fare", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        busTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(busTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Booking Panel
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JLabel seatsLabel = new JLabel("Number of Seats:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        seatsSpinner = new JSpinner(spinnerModel);
        bookButton = new JButton("Book Selected Bus");
        bookButton.setEnabled(false);
        
        bookingPanel.add(seatsLabel);
        bookingPanel.add(seatsSpinner);
        bookingPanel.add(bookButton);
        
        // Add panels to main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bookingPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBuses();
            }
        });
        
        busTable.getSelectionModel().addListSelectionListener(e -> {
            bookButton.setEnabled(busTable.getSelectedRow() != -1);
        });
        
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookSelectedBus();
            }
        });
    }
    
    private void searchBuses() {
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();
        String dateStr = dateField.getText().trim();
        
        if (source.isEmpty() || destination.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all search fields", 
                                         "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = format.parse(dateStr);
            Date journeyDate = new Date(parsedDate.getTime());
            
            List<Bus> buses = busController.searchBuses(source, destination, journeyDate);
            updateBusTable(buses);
            
            if (buses.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No buses found for the given criteria", 
                                             "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd", 
                                         "Date Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBusTable(List<Bus> buses) {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add buses to table
        for (Bus bus : buses) {
            Object[] row = {
                bus.getBusId(),
                bus.getBusNumber(),
                bus.getBusName(),
                bus.getSourceLocation(),
                bus.getDestination(),
                bus.getDepartureTime(),
                bus.getArrivalTime(),
                bus.getJourneyDate(),
                bus.getAvailableSeats(),
                bus.getFare(),
                bus.getBusType()
            };
            tableModel.addRow(row);
        }
    }
    
    private void bookSelectedBus() {
        int selectedRow = busTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        int busId = (int) tableModel.getValueAt(selectedRow, 0);
        int availableSeats = (int) tableModel.getValueAt(selectedRow, 8);
        double fare = (double) tableModel.getValueAt(selectedRow, 9);
        Date journeyDate = (Date) tableModel.getValueAt(selectedRow, 7);
        int numberOfSeats = (int) seatsSpinner.getValue();
        
        if (numberOfSeats > availableSeats) {
            JOptionPane.showMessageDialog(this, "Not enough seats available", 
                                         "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calculate total fare
        double totalFare = fare * numberOfSeats;
        
        // Confirm booking
        int option = JOptionPane.showConfirmDialog(this, 
                "Confirm booking for " + numberOfSeats + " seats?\nTotal fare: $" + totalFare, 
                "Booking Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            // Create reservation
            Reservation reservation = new Reservation();
            reservation.setUserId(currentUser.getUserId());
            reservation.setBusId(busId);
            reservation.setJourneyDate(journeyDate);
            reservation.setNumberOfSeats(numberOfSeats);
            reservation.setTotalFare(totalFare);
            reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
            
            int reservationId = reservationController.createReservation(reservation);
            
            if (reservationId > 0) {
                JOptionPane.showMessageDialog(this, 
                        "Booking successful!\nReservation ID: " + reservationId, 
                        "Booking Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the bus list
                searchBuses();
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed. Please try again.", 
                                             "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

