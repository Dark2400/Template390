package com.projects.marc.template390;

import java.sql.Time;
/*
ConfigTable is a container to pass bulk data between MySQLHelper and other objects
 */
public class ConfigTable {

    //private data members mirror config_table columns
    private int desiredMoisture;
    private int minimumPumpVolume;
    private Time lightStart;
    private Time lightStop;
    private boolean disableFlood;

    //constructor builds object with parameters
    public ConfigTable(int moistureSetting, int pumpSetting, Time lightOnTime, Time lightOffTime, boolean toDisable){
        setDesiredMoisture(moistureSetting);
        setMinimumPumpVolume(pumpSetting);
        setLightStart(lightOnTime);
        setLightStop(lightOffTime);
        setDisableFlood(toDisable);
    }
    /*
    Remaining functions are typical get/set functions
     */
    public int getDesiredMoisture() {
        return desiredMoisture;
    }

    private void setDesiredMoisture(int newDesiredMoisture) {
        desiredMoisture = newDesiredMoisture;
    }

    public int getMinimumPumpVolume() {
        return minimumPumpVolume;
    }

    private void setMinimumPumpVolume(int newMinimumPumpVolume) {
        minimumPumpVolume = newMinimumPumpVolume;
    }

    public Time getLightStart() {
        return lightStart;
    }

    private void setLightStart(Time lightStart) {
        this.lightStart = lightStart;
    }

    public Time getLightStop() {
        return lightStop;
    }

    private void setLightStop(Time lightStop) {
        this.lightStop = lightStop;
    }

    public boolean getDisableFlood(){return disableFlood;}

    private void setDisableFlood(boolean toDisable){ disableFlood = toDisable;};

}