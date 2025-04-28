package com.busreservation.dao;

import com.busreservation.model.Bus;
import com.busreservation.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {
    private Connection connection;
    
    public BusDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public boolean addBus(Bus bus) {
        String query = "INSERT INTO buses (bus_number, bus_name, source_location, destination, " +
                       "departure_time, arrival_time, journey_date, total_seats, available_seats, " +
                       "fare, bus_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bus.getBusNumber());
            stmt.setString(2, bus.getBusName());
            stmt.setString(3, bus.getSourceLocation());
            stmt.setString(4, bus.getDestination());
            stmt.setTime(5, bus.getDepartureTime());
            stmt.setTime(6, bus.getArrivalTime());
            stmt.setDate(7, bus.getJourneyDate());
            stmt.setInt(8, bus.getTotalSeats());
            stmt.setInt(9, bus.getAvailableSeats());
            stmt.setDouble(10, bus.getFare());
            stmt.setString(11, bus.getBusType().toString());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    bus.setBusId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding bus: " + e.getMessage());
        }
        return false;
    }
    
    public Bus getBusById(int busId) {
        String query = "SELECT * FROM buses WHERE bus_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, busId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBusFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting bus by ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String query = "SELECT * FROM buses";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                buses.add(extractBusFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all buses: " + e.getMessage());
        }
        return buses;
    }
    
    public List<Bus> searchBuses(String source, String destination, Date journeyDate) {
        List<Bus> buses = new ArrayList<>();
        String query = "SELECT * FROM buses WHERE source_location LIKE ? AND destination LIKE ? " +
                       "AND journey_date = ? AND available_seats > 0";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + source + "%");
            stmt.setString(2, "%" + destination + "%");
            stmt.setDate(3, journeyDate);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                buses.add(extractBusFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching buses: " + e.getMessage());
        }
        return buses;
    }
    
    public boolean updateBus(Bus bus) {
        String query = "UPDATE buses SET bus_number = ?, bus_name = ?, source_location = ?, " +
                       "destination = ?, departure_time = ?, arrival_time = ?, journey_date = ?, " +
                       "total_seats = ?, available_seats = ?, fare = ?, bus_type = ? WHERE bus_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bus.getBusNumber());
            stmt.setString(2, bus.getBusName());
            stmt.setString(3, bus.getSourceLocation());
            stmt.setString(4, bus.getDestination());
            stmt.setTime(5, bus.getDepartureTime());
            stmt.setTime(6, bus.getArrivalTime());
            stmt.setDate(7, bus.getJourneyDate());
            stmt.setInt(8, bus.getTotalSeats());
            stmt.setInt(9, bus.getAvailableSeats());
            stmt.setDouble(10, bus.getFare());
            stmt.setString(11, bus.getBusType().toString());
            stmt.setInt(12, bus.getBusId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating bus: " + e.getMessage());
        }
        return false;
    }
    
    public boolean deleteBus(int busId) {
        String query = "DELETE FROM buses WHERE bus_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, busId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting bus: " + e.getMessage());
        }
        return false;
    }
    
    public boolean updateAvailableSeats(int busId, int seatsToReduce) {
        String query = "UPDATE buses SET available_seats = available_seats - ? WHERE bus_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, seatsToReduce);
            stmt.setInt(2, busId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating available seats: " + e.getMessage());
        }
        return false;
    }
    
    private Bus extractBusFromResultSet(ResultSet rs) throws SQLException {
        Bus bus = new Bus();
        bus.setBusId(rs.getInt("bus_id"));
        bus.setBusNumber(rs.getString("bus_number"));
        bus.setBusName(rs.getString("bus_name"));
        bus.setSourceLocation(rs.getString("source_location"));
        bus.setDestination(rs.getString("destination"));
        bus.setDepartureTime(rs.getTime("departure_time"));
        bus.setArrivalTime(rs.getTime("arrival_time"));
        bus.setJourneyDate(rs.getDate("journey_date"));
        bus.setTotalSeats(rs.getInt("total_seats"));
        bus.setAvailableSeats(rs.getInt("available_seats"));
        bus.setFare(rs.getDouble("fare"));
        bus.setBusType(Bus.BusType.valueOf(rs.getString("bus_type")));
        return bus;
    }
}

       