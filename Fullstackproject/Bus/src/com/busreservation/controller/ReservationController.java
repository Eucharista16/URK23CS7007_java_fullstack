package com.busreservation.controller;

import com.busreservation.dao.ReservationDAO;
import com.busreservation.model.Reservation;

import java.util.List;

public class ReservationController {
    private ReservationDAO reservationDAO;
    
    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
    }
    
    public int createReservation(Reservation reservation) {
        return reservationDAO.createReservation(reservation);
    }
    
    public Reservation getReservationById(int reservationId) {
        return reservationDAO.getReservationById(reservationId);
    }
    
    public List<Reservation> getReservationsByUserId(int userId) {
        return reservationDAO.getReservationsByUserId(userId);
    }
    
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }
    
    public boolean updateReservationStatus(int reservationId, Reservation.ReservationStatus status) {
        return reservationDAO.updateReservationStatus(reservationId, status);
    }
    
    public boolean cancelReservation(int reservationId) {
        return reservationDAO.updateReservationStatus(reservationId, Reservation.ReservationStatus.CANCELLED);
    }
    
    public boolean deleteReservation(int reservationId) {
        return reservationDAO.deleteReservation(reservationId);
    }
}
