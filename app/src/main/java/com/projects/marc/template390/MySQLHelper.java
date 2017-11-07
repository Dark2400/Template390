package com.projects.marc.template390;


import android.util.Log;
import java.sql.*;
import java.lang.*;

/**
** MySQLHelper is used to facilitate server read and writes easily
 * */
class MySQLHelper{
        //server config data
        private final static String LOCALHOST = "192.168.2.200";
        private final static String USERNAME = "teamlogin";
        private final static String PASSWORD = "coen390";
        private final static String DRIVER = "org.mariadb.jdbc.Driver";
        private final static String DATABASE_NAME = "greenteam";

        //table constants
        protected final static String DEBUG_TABLE = "debug_table";
        private final static String CONFIG_TABLE = "config_table";
        private final static String SHORT_TABLE = "shortterm_table";
        private final static String LONG_TABLE = "longterm_table";

        // Config table columns
        private static final String KEY_DESIRED_MOISTURE = "desired_moisture";
        private static final String KEY_PUMP_MIN = "minimum_pump_volume";
        private static final String KEY_LIGHT_START = "light_start_time";
        private static final String KEY_LIGHT_STOP = "light_stop_time";
        private static final String KEY_FLOOD = "disable_flood";

        // Debug table columns
        protected static final String KEY_PUMP_TEST = "pump_test";
        protected static final String KEY_LIGHT_TEST = "light_test";
        protected static final String KEY_DELAY = "timer_delay";

        // Short term table columns
        private static final String KEY_ID = "id";
        private static final String KEY_WATER_TIME = "time_watered";
        private static final String KEY_WATER_DATE = "date";
        private static final String KEY_MOISTURE_LEVEL = "moisture_level";
        private static final String KEY_LIGHT_STATUS = "light_status";
        private static final String KEY_RESERVOIR_LOW = "reservoir_low";

        //Long term table columns
        private static final String KEY_AVERAGE = "average_moisture";
        private static final String KEY_DATE = "date";

        //Static connection for use in entire object
        private static Connection con;

        //shortTermIndex variables used for translating shortterm into longterm
        private static int shortTermIndex = 0;
        private static final int MAX_SHORT_TERM_LENGTH = 5;

        //MySQLHelper is initialized using setup function
    public MySQLHelper() throws Exception{
            setup();
        }

        //Setup function initializes con object using server constants

    private void setup() throws Exception
    {
        String CONNECTION = "jdbc:mysql://" + LOCALHOST + ":3306/" + DATABASE_NAME;
        try {
            // Registers driver
            Class.forName(DRIVER).newInstance();
            // Open connection
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            if(con.isValid(0)) {
                System.out.println("Connected to MySQL server");
            }
        } catch (Exception e) {
            Log.e("Exception", "Setup: " + e);
            throw e;
        }
    }

    //getConfig pulls entire config_table, separates all columns into variables of their type
    //then returns a configTable using the column data
    public ConfigTable getConfig()  {
        ConfigTable dbConfig = null;
       try {
           //create statement for query
           Statement db = con.createStatement();
           //create ResultSet from query
           ResultSet rs = db.executeQuery("SELECT * FROM " + CONFIG_TABLE + " WHERE id = 1");
           //rs.next() only returns has one element, so while only runs once
           while (rs.next()) {
               //get column data and build ConfigTable to be returned
               int dbMoistureSetting = rs.getInt(KEY_DESIRED_MOISTURE);
               int dbMinimumPump = rs.getInt(KEY_PUMP_MIN);
               Time dbLightOn = rs.getTime(KEY_LIGHT_START);
               Time dbLightOff = rs.getTime(KEY_LIGHT_STOP);
               boolean disableFlood = rs.getBoolean(KEY_FLOOD);
               dbConfig = new ConfigTable(dbMoistureSetting, dbMinimumPump, dbLightOn, dbLightOff, disableFlood);
           }
           //close statement and resultset before returning ConfigTable
           rs.close();
           db.close();
       }catch (Exception e){System.out.println(e);}
        return dbConfig;
    }
    /**
     *  Get table item count
     */
    public int getTableItemCount(String table)
    {

        int count = 0;
        //create statement for query
        try {
            Statement db = con.createStatement();
            //create ResultSet from query
            ResultSet rs = db.executeQuery("SELECT * FROM " + table);
            while (rs.next()) {
                count++;
            }
        } catch (Exception e ){Log.e("Exception","Error: " + e);}
        return count;
    }


    /**
     * get Short term table and update the values the class
     */

    public ShorttermTable getShortTermTable(int index)  { //throws Exception

        ShorttermTable dbShortterm = null;
        try {
            //create statement for query
            Statement db = con.createStatement();
            //create ResultSet from query
            ResultSet rs = db.executeQuery("SELECT * FROM " + SHORT_TABLE + " WHERE id = " + index);
            while (rs.next()) {
                //get column data and build ShortTermTable to be returned
                int id = rs.getInt(KEY_ID);
                Time TimeWatered = rs.getTime(KEY_WATER_TIME);
                int MoistureLevel = rs.getInt(KEY_MOISTURE_LEVEL);
                boolean ReservoirLow = rs.getBoolean(KEY_RESERVOIR_LOW);
                boolean LightStatus = rs.getBoolean(KEY_LIGHT_STATUS);
                Date date = rs.getDate(KEY_WATER_DATE);
                dbShortterm = new ShorttermTable(id, TimeWatered, MoistureLevel, ReservoirLow, LightStatus, date);
            }
            //close statement and result set before returning short term table
            rs.close();
            db.close();

        }catch(Exception e) {Log.e("Exception", "getShortTermTable ",e);}
        return dbShortterm;
    }

    public LongtermTable getLongTermTable(int index)  { //throws Exception

        LongtermTable dbLongterm = null;
        try {
            //create statement for query
            Statement db = con.createStatement();
            //create ResultSet from query
            ResultSet rs = db.executeQuery("SELECT * FROM " + LONG_TABLE + " WHERE id = " + index);
            while (rs.next()) {
                //get column data and build LongTermTable to be returned
                int id = rs.getInt(KEY_ID);
                float avg_moisture = rs.getFloat(KEY_AVERAGE);
                Date date = rs.getDate(KEY_DATE);
                dbLongterm = new LongtermTable(id, avg_moisture, date);
            }
            //close statement and result set before returning Long term table
            rs.close();
            db.close();

        }catch(Exception e) {Log.e("Exception", "getShortTermTable ",e);}
        return dbLongterm;
    }

	/*
	getDebug is currently unused - will be refactored to return DebugTable object in the future
	*/

	/*
	public Object[] getDebug(){
		Object[] debugTable = new Object[3];
		try {
		    Statement db = con.createStatement();
			ResultSet rs = db.executeQuery("SELECT * FROM " + DEBUG_TABLE);
			rs.next();
			for (int i = 0; i < 3; i++){
				//System.out.println("Line " + i);
				debugTable[i] = rs.getObject(i + 2);
				//System.out.println("Object[" + i + "]: " + debugTable[i]);
			}
			rs.close();
            con.close();
		}
		catch (Exception e) {
            System.out.println(e);
        }
		return debugTable;
	}
	*/

    //Updates server's shortterm_table with provided data
    public void updateShortTerm(Time waterTime, int moistureLevel, boolean lightStatus, boolean reservoirLow, Date currentDate) {
        //updates longterm_table and resets index of shortterm_table row if shortterm_length is reached
        if (shortTermIndex >= MAX_SHORT_TERM_LENGTH) {
            updateLongTerm();
            shortTermIndex = 0;
        }
        //updates appropriate shortterm_table row with column data
        try {
            Statement db = con.createStatement();
            db.execute("UPDATE " + SHORT_TABLE +
                    " SET " + KEY_WATER_TIME + " = '" + waterTime +
                    "', " + KEY_MOISTURE_LEVEL + " = '" + moistureLevel +
                    "', " + KEY_LIGHT_STATUS + " = '" + (lightStatus ? 1 : 0) +
                    "', " + KEY_RESERVOIR_LOW + " = '" + (reservoirLow ? 1 : 0) +
                    "', " + KEY_WATER_DATE + " = '" + currentDate +
                    "' WHERE id = '" + ++shortTermIndex + "'");
            db.close();
        } catch (Exception e) {
            Log.e("Exception", "updateShortTerm: " + e);
        }

    }

    //Averages all elements in shortterm_table and uploads it to longterm_table
    //Will be refactored to have limited length and work as circular queue in the future
    private void updateLongTerm() {
        int counter = 0;
        float sum = 0;
        try {
            Statement db = con.createStatement();
            ResultSet rs = db.executeQuery("SELECT " + KEY_MOISTURE_LEVEL + " FROM " + SHORT_TABLE);
            while (rs.next()) {
                sum += rs.getInt(1);
                counter++;
            }
            db.execute("INSERT INTO " + LONG_TABLE + " (" + KEY_AVERAGE + ") VALUE (" + sum / counter + ")");
            System.out.println("Inserted moisture average: " + sum / counter);
            rs.close();
            db.close();
        } catch (Exception e) {
            Log.e("Exception", "updateLongTerm: " + e);
        }
    }

    //Updates boolean column of whichever table/key is provided with boolean value
    public void updateBoolean(boolean value, String table, String key) {
        try {
            Statement db = con.createStatement();
            db.executeUpdate("UPDATE " + table + " SET " + key + " = " + (value ? 1 : 0));
            db.close();
        } catch (SQLException e) {
            Log.e("Exception", "updateBoolean: " + e);
        }
    }

    public void updateConfigTable(ConfigTable input){
        try {
            Statement db = con.createStatement();
            db.executeUpdate("UPDATE " + CONFIG_TABLE
                    + " SET " + KEY_DESIRED_MOISTURE + " = '" + input.getDesiredMoisture()
                    + "', " + KEY_PUMP_MIN + " = '" + input.getMinimumPumpVolume()
                    + "', " + KEY_LIGHT_START + " = '" + input.getLightStart()
                    + "', " + KEY_LIGHT_STOP + " = '" + input.getLightStop()
                    + "', " + KEY_FLOOD + " = '" + (input.getDisableFlood() ? 1 : 0) + "'"
            );
            db.close();
        } catch (SQLException e) {
            Log.e("Exception", "updateBoolean: " + e);
        }
    }

    //closes db connection
    public void close() throws Exception {
        con.close();
    }
}