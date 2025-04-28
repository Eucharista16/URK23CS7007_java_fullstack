package com.busreservation.dao;

import com.busreservation.model.Reservation;
import com.busreservation.model.Bus;
import com.busreservation.model.User;
import com.busreservation.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private Connection connection;
    private BusDAO busDAO;
    private UserDAO userDAO;
    
    public ReservationDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.busDAO = new BusDAO();
        this.userDAO = new UserDAO();
    }
    
    public int createReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (user_id, bus_id, journey_date, number_of_seats, " +
                       "total_fare, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getBusId());
            stmt.setDate(3, reservation.getJourneyDate());
            stmt.setInt(4, reservation.getNumberOfSeats());
            stmt.setDouble(5, reservation.getTotalFare());
            stmt.setString(6, reservation.getStatus().toString());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int reservationId = generatedKeys.getInt(1);
                    reservation.setReservationId(reservationId);
                    
                    // Update available seats in the bus
                    busDAO.updateAvailableSeats(reservation.getBusId(), reservation.getNumberOfSeats());
                    
                    return reservationId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating reservation: " + e.getMessage());
        }
        return -1;
    }
    
    public Reservation getReservationById(int reservationId) {
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Reservation reservation = extractReservationFromResultSet(rs);
                
                // Load related objects
                Bus bus = busDAO.getBusById(reservation.getBusId());
                User user = userDAO.getUserById(reservation.getUserId());
                
                reservation.setBus(bus);
                reservation.setUser(user);
                
                return reservation;
            }
        } catch (SQLException e) {
            System.err.println("Error getting reservation by ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = extractReservationFromResultSet(rs);
                
                // Load related bus
                Bus bus = busDAO.getBusById(reservation.getBusId());
                reservation.setBus(bus);
                
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error getting reservations by user ID: " + e.getMessage());
        }
        return reservations;
    }
    
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Reservation reservation = extractReservationFromResultSet(rs);
                
                // Load related objects
                Bus bus = busDAO.getBusById(reservation.getBusId());
                User user = userDAO.getUserById(reservation.getUserId());
                
                reservation.setBus(bus);
                reservation.setUser(user);
                
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all reservations: " + e.getMessage());
        }
        return reservations;
    }
    
    public boolean updateReservationStatus(int reservationId, Reservation.ReservationStatus status) {
        String query = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status.toString());
            stmt.setInt(2, reservationId);
            
            int affectedRows = stmt.executeUpdate();
            
            // If cancelling, update available seats
            if (status == Reservation.ReservationStatus.CANCELLED) {
                Reservation reservation = getReservationById(reservationId);
                if (reservation != null) {
                    // Add seats back to available
                    Bus bus = busDAO.getBusById(reservation.getBusId());
                    bus.setAvailableSeats(bus.getAvailableSeats() + reservation.getNumberOfSeats());
                    busDAO.updateBus(bus);
                }
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating reservation status: " + e.getMessage());
        }
        return false;
    }
    
    public boolean deleteReservation(int reservationId) {
        // First get the reservation to know how many seats to add back
        Reservation reservation = getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }
        
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Add seats back to available
                Bus bus = busDAO.getBusById(reservation.getBusId());
                bus.setAvailableSeats(bus.getAvailableSeats() + reservation.getNumberOfSeats());
                busDAO.updateBus(bus);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
        }
        return false;
    }
    
    private Reservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setUserId(rs.getInt("user_id"));
        reservation.setBusId(rs.getInt("bus_id"));
        reservation.setBookingDate(rs.getTimestamp("booking_date"));
        reservation.setJourneyDate(rs.getDate("journey_date"));
        reservation.setNumberOfSeats(rs.getInt("number_of_seats"));
        reservation.setTotalFare(rs.getDouble("total_fare"));
        reservation.setStatus(Reservation.ReservationStatus.valueOf(rs.getString("status")));
        return reservation;
    }
}
