package com.busreservation.model;

import java.sql.Time;
import java.sql.Date;

public class Bus {
    private int busId;
    private String busNumber;
    private String busName;
    private String sourceLocation;
    private String destination;
    private Time departureTime;
    private Time arrivalTime;
    private Date journeyDate;
    private int totalSeats;
    private int availableSeats;
    private double fare;
    private BusType busType;
    
    public enum BusType {
        AC, NON_AC, SLEEPER, SEMI_SLEEPER
    }
    
    // Default constructor
    public Bus() {
    }
    
    // Constructor with fields
    public Bus(int busId, String busNumber, String busName, String sourceLocation, String destination,
               Time departureTime, Time arrivalTime, Date journeyDate, int totalSeats, 
               int availableSeats, double fare, BusType busType) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.busName = busName;
        this.sourceLocation = sourceLocation;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.journeyDate = journeyDate;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.fare = fare;
        this.busType = busType;
    }
    
    // Getters and Setters
    public int getBusId() {
        return busId;
    }
    
    public void setBusId(int busId) {
        this.busId = busId;
    }
    
    public String getBusNumber() {
        return busNumber;
    }
    
    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }
    
    public String getBusName() {
        return busName;
    }
    
    public void setBusName(String busName) {
        this.busName = busName;
    }
    
    public String getSourceLocation() {
        return sourceLocation;
    }
    
    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public Time getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }
    
    public Time getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public Date getJourneyDate() {
        return journeyDate;
    }
    
    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }
    
    public int getTotalSeats() {
        return totalSeats;
    }
    
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public double getFare() {
        return fare;
    }
    
    public void setFare(double fare) {
        this.fare = fare;
    }
    
    public BusType getBusType() {
        return busType;
    }
    
    public void setBusType1(BusType busType) {
        this.busType = busType;
    }
    public void setBusType11(BusType busType) {
    	this.busType = busType;
        }
    
    public void setBusType(BusType busType) {
        this.busType = busType;
    }
    
    @Override
    public String toString() {
        return "Bus{" +
                "busId=" + busId +
                ", busNumber='" + busNumber + '\'' +
                ", busName='" + busName + '\'' +
                ", sourceLocation='" + sourceLocation + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", journeyDate=" + journeyDate +
                ", availableSeats=" + availableSeats +
                ", fare=" + fare +
                ", busType=" + busType +
                '}';
    }
}


