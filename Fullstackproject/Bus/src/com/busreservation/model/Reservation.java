package com.busreservation.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {
    private int reservationId;
    private int userId;
    private int busId;
    private Timestamp bookingDate;
    private Date journeyDate;
    private int numberOfSeats;
    private double totalFare;
    private ReservationStatus status;
    
    // Reference to related objects
    private User user;
    private Bus bus;
    
    public enum ReservationStatus {
        CONFIRMED, CANCELLED, PENDING
    }
    
    // Default constructor
    public Reservation() {
    }
    
    // Constructor with fields
    public Reservation(int reservationId, int userId, int busId, Timestamp bookingDate, 
                      Date journeyDate, int numberOfSeats, double totalFare, ReservationStatus status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.busId = busId;
        this.bookingDate = bookingDate;
        this.journeyDate = journeyDate;
        this.numberOfSeats = numberOfSeats;
        this.totalFare = totalFare;
        this.status = status;
    }
    
    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getBusId() {
        return busId;
    }
    
    public void setBusId(int busId) {
        this.busId = busId;
    }
    
    public Timestamp getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public Date getJourneyDate() {
        return journeyDate;
    }
    
    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }
    
    public int getNumberOfSeats() {
        return numberOfSeats;
    }
    
    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
    
    public double getTotalFare() {
        return totalFare;
    }
    
    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Bus getBus() {
        return bus;
    }
    
    public void setBus(Bus bus) {
        this.bus = bus;
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", userId=" + userId +
                ", busId=" + busId +
                ", bookingDate=" + bookingDate +
                ", journeyDate=" + journeyDate +
                ", numberOfSeats=" + numberOfSeats +
                ", totalFare=" + totalFare +
                ", status=" + status +
                '}';
    }
}
