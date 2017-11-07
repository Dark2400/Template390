package com.projects.marc.template390;


import java.util.Date;

public class LongtermTable {
    /** Long Term Table Fields */
    private int id;
    private float average_moisture;
    private Date date;


    public LongtermTable() {
        id = 1;
        average_moisture = 0.0f;
        date = new Date();
    }
    public LongtermTable(int Id, float avg_moisture, Date newdate ) {
        id = Id;
        average_moisture = avg_moisture;
        date = newdate;

    }


    public void setId(int Id){ id = Id;}
    public void setAverage_moisture(float avg_moisture){average_moisture = avg_moisture;}
    public void setDate_time(Date newdate){date = newdate;}

    public int getId(){return id;}
    public float getAverage_moisture(){return average_moisture;}
    public Date getDate(){return date;}

}

