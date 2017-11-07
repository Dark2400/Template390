package com.projects.marc.template390;

import java.sql.Date;
import java.sql.Time;



public class ShorttermTable {
    private int id;
    private Time time_watered;
    private int moisture_level;
    private boolean reservoir_low;
    private boolean light_status;
    private Date date;


    public ShorttermTable(int id, Time time_watered, int moisture_level, boolean reservoir_low, boolean light_status, Date date)
    {
        setId(id);
        setTimeWatered(time_watered);
        setMoistureLevel(moisture_level);
        setReservoirLow(reservoir_low);
        setLightStatus(light_status);
        setDate(date);
    }
    public ShorttermTable()
    {
        setId(1);
        setTimeWatered(null);
        setMoistureLevel(0);
        setReservoirLow(false);
        setLightStatus(false);
        setDate(null);
    }
    public int getId() { return id; }

    public Time getTimeWatered() {
        return time_watered;
    }

    public int getMoistureLevel() {
        return moisture_level;
    }

    public boolean getReservoirLow() {
        return reservoir_low;
    }

    public boolean getLightStatus() {
        return light_status;
    }

    public Date getDate() {
        return date;
    }

    public String getAll() {return ("ID: " + id + " Time Watered: " + time_watered + " Moisture Level: " + moisture_level + " Reservoir Low: " + reservoir_low + " Light Status: " + light_status + " Date: " + date);}


    private void setId(int newId) {
        id = newId;
    }

    private void setTimeWatered(Time newTimeWatered) {
        time_watered = newTimeWatered;
    }

    private void setMoistureLevel(int newMoistureLevel) { moisture_level = newMoistureLevel; }

    private void setReservoirLow(boolean newReservoirLow) {
        reservoir_low = newReservoirLow;
    }

    private void setLightStatus(boolean newLightStatus) {
        light_status = newLightStatus;
    }

    private void setDate(Date newDate) {
        date = newDate;
    }





}
