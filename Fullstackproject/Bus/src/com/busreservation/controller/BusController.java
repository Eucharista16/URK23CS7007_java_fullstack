package com.busreservation.controller;

import com.busreservation.dao.BusDAO;
import com.busreservation.model.Bus;

import java.sql.Date;
import java.util.List;

public class BusController {
    private BusDAO busDAO;
    
    public BusController() {
        this.busDAO = new BusDAO();
    }
    
    public boolean addBus(Bus bus) {
        return busDAO.addBus(bus);
    }
    
    public Bus getBusById(int busId) {
        return busDAO.getBusById(busId);
    }
    
    public List<Bus> getAllBuses() {
        return busDAO.getAllBuses();
    }
    
    public List<Bus> searchBuses(String source, String destination, Date journeyDate) {
        return busDAO.searchBuses(source, destination, journeyDate);
    }
    
    public boolean updateBus(Bus bus) {
        return busDAO.updateBus(bus);
    }
    
    public boolean deleteBus(int busId) {
        return busDAO.deleteBus(busId);
    }
}
