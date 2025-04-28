package com.busreservation.view;

import com.busreservation.controller.BusController;
import com.busreservation.model.Bus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BusManagementView extends JFrame {
    private JTable busTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private BusController busController;
    
    public BusManagementView() {
        busController = new BusController();
        initComponents();
        loadBuses();
    }
    
    private void initComponents() {
        setTitle("Bus Reservation System - Bus Management");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Bus Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        
        // Create table model with columns
        String[] columns = {"Bus ID", "Bus Number", "Bus Name", "From", "To", "Departure", "Arrival", "Journey Date", "Total Seats", "Available Seats", "Fare", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        busTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(busTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        addButton = new JButton("Add Bus");
        editButton = new JButton("Edit Bus");
        deleteButton = new JButton("Delete Bus");
        refreshButton = new JButton("Refresh");
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
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
                showBusDialog(null);
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedBus();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBus();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBuses();
            }
        });
        
        busTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = busTable.getSelectedRow() != -1;
            editButton.setEnabled(rowSelected);
            deleteButton.setEnabled(rowSelected);
        });
    }
    
    private void loadBuses() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get all buses
        List<Bus> buses = busController.getAllBuses();
        
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
                bus.getTotalSeats(),
                bus.getAvailableSeats(),
                bus.getFare(),
                bus.getBusType()
            };
            tableModel.addRow(row);
        }
    }
    
    private void editSelectedBus() {
        int selectedRow = busTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        int busId = (int) tableModel.getValueAt(selectedRow, 0);
        Bus bus = busController.getBusById(busId);
        
        if (bus != null) {
            showBusDialog(bus);
        }
    }
    
    private void deleteSelectedBus() {
        int selectedRow = busTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        int busId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Confirm deletion
        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this bus?", 
                "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            boolean success = busController.deleteBus(busId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Bus deleted successfully", 
                                             "Delete Success", JOptionPane.INFORMATION_MESSAGE);
                loadBuses(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bus. It may have reservations.", 
                                             "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showBusDialog(Bus bus) {
        // Create a dialog for adding/editing bus
        JDialog dialog = new JDialog(this, bus == null ? "Add New Bus" : "Edit Bus", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form fields
        JLabel busNumberLabel = new JLabel("Bus Number:");
        JTextField busNumberField = new JTextField(20);
        
        JLabel busNameLabel = new JLabel("Bus Name:");
        JTextField busNameField = new JTextField(20);
        
        JLabel sourceLabel = new JLabel("From:");
        JTextField sourceField = new JTextField(20);
        
        JLabel destinationLabel = new JLabel("To:");
        JTextField destinationField = new JTextField(20);
        
        JLabel departureLabel = new JLabel("Departure Time (HH:mm):");
        JTextField departureField = new JTextField(20);
        
        JLabel arrivalLabel = new JLabel("Arrival Time (HH:mm):");
        JTextField arrivalField = new JTextField(20);
        
        JLabel dateLabel = new JLabel("Journey Date (yyyy-MM-dd):");
        JTextField dateField = new JTextField(20);
        
        JLabel totalSeatsLabel = new JLabel("Total Seats:");
        JSpinner totalSeatsSpinner = new JSpinner(new SpinnerNumberModel(40, 1, 100, 1));
        
        JLabel fareLabel = new JLabel("Fare ($):");
        JSpinner fareSpinner = new JSpinner(new SpinnerNumberModel(50.0, 1.0, 1000.0, 0.5));
        
        JLabel busTypeLabel = new JLabel("Bus Type:");
        JComboBox<Bus.BusType> busTypeCombo = new JComboBox<>(Bus.BusType.values());
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        // If editing, populate fields with bus data
        if (bus != null) {
            busNumberField.setText(bus.getBusNumber());
            busNameField.setText(bus.getBusName());
            sourceField.setText(bus.getSourceLocation());
            destinationField.setText(bus.getDestination());
            departureField.setText(bus.getDepartureTime().toString());
            arrivalField.setText(bus.getArrivalTime().toString());
            dateField.setText(bus.getJourneyDate().toString());
            totalSeatsSpinner.setValue(bus.getTotalSeats());
            fareSpinner.setValue(bus.getFare());
            busTypeCombo.setSelectedItem(bus.getBusType());
        }
        
        // Add components to panel
        panel.add(busNumberLabel);
        panel.add(busNumberField);
        panel.add(busNameLabel);
        panel.add(busNameField);
        panel.add(sourceLabel);
        panel.add(sourceField);
        panel.add(destinationLabel);
        panel.add(destinationField);
        panel.add(departureLabel);
        panel.add(departureField);
        panel.add(arrivalLabel);
        panel.add(arrivalField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(totalSeatsLabel);
        panel.add(totalSeatsSpinner);
        panel.add(fareLabel);
        panel.add(fareSpinner);
        panel.add(busTypeLabel);
        panel.add(busTypeCombo);
        panel.add(saveButton);
        panel.add(cancelButton);
        
        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validate and parse input
                    String busNumber = busNumberField.getText().trim();
                    String busName = busNameField.getText().trim();
                    String source = sourceField.getText().trim();
                    String destination = destinationField.getText().trim();
                    String departureStr = departureField.getText().trim();
                    String arrivalStr = arrivalField.getText().trim();
                    String dateStr = dateField.getText().trim();
                    
                    if (busNumber.isEmpty() || busName.isEmpty() || source.isEmpty() || 
                        destination.isEmpty() || departureStr.isEmpty() || 
                        arrivalStr.isEmpty() || dateStr.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "All fields are required", 
                                                     "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Parse time and date
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    
                    Time departureTime = new Time(timeFormat.parse(departureStr).getTime());
                    Time arrivalTime = new Time(timeFormat.parse(arrivalStr).getTime());
                    Date journeyDate = new Date(dateFormat.parse(dateStr).getTime());
                    
                    int totalSeats = (int) totalSeatsSpinner.getValue();
                    double fare = (double) fareSpinner.getValue();
                    Bus.BusType busType = (Bus.BusType) busTypeCombo.getSelectedItem();
                    
                    // Create or update bus object
                    Bus busToBeSaved;
                    if (bus == null) {
                        busToBeSaved = new Bus();
                        busToBeSaved.setAvailableSeats(totalSeats); // For new bus, all seats are available
                    } else {
                        busToBeSaved = bus;
                        // For existing bus, calculate available seats
                        int bookedSeats = bus.getTotalSeats() - bus.getAvailableSeats();
                        busToBeSaved.setAvailableSeats(totalSeats - bookedSeats);
                    }
                    
                    busToBeSaved.setBusNumber(busNumber);
                    busToBeSaved.setBusName(busName);
                    busToBeSaved.setSourceLocation(source);
                    busToBeSaved.setDestination(destination);
                    busToBeSaved.setDepartureTime(departureTime);
                    busToBeSaved.setArrivalTime(arrivalTime);
                    busToBeSaved.setJourneyDate(journeyDate);
                    busToBeSaved.setTotalSeats(totalSeats);
                    busToBeSaved.setFare(fare);
                    busToBeSaved.setBusType(busType);
                    
                    boolean success;
                    if (bus == null) {
                        success = busController.addBus(busToBeSaved);
                    } else {
                        success = busController.updateBus(busToBeSaved);
                    }
                    
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Bus saved successfully", 
                                                     "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadBuses(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to save bus", 
                                                     "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid time or date format", 
                                                 "Format Error", JOptionPane.ERROR_MESSAGE);
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

