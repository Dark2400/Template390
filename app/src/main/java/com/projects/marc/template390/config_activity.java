package com.projects.marc.template390;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Timer;

import static android.R.drawable.checkbox_off_background;
import static android.R.drawable.checkbox_on_background;

public class config_activity extends AppCompatActivity {

    protected static Toolbar myToolbar;
    protected static Button saveButton;
    protected static EditText moistureText;
    protected static EditText amountText;
    protected static EditText startText;
    protected static EditText stopText;
//    protected static MySQLHelper helper;
    protected static ToggleButton toggleFlood;
//    protected static boolean toggled = false;
    protected static MenuItem mainMenuItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_activity);
        setupUI();
    }

    protected void onStart(){
        super.onStart();
        loadCurrent();
//        makeEditable(false);
    }


    protected void setupUI(){
        Log.e("setup", "here");
        saveButton = (Button) findViewById(R.id.saveButton);
        moistureText = (EditText) findViewById(R.id.moistureInput);
        amountText = (EditText) findViewById(R.id.amountInput);
        startText = (EditText) findViewById(R.id.startInput);
        stopText = (EditText) findViewById(R.id.stopInput);
        toggleFlood = (ToggleButton) findViewById(R.id.floodButton);

//        try{
//            helper = new MySQLHelper();
//        }catch(Exception e){
//            Log.e("Activity_config", "Setup: " + e);
//        }
//        ConfigTable temp = helper.getConfig();
//        Log.e("Config", "Stuff:\t" + temp.getDesiredMoisture() + "\t" + temp.getDesiredMoisture());


        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

    }

    protected void loadCurrent(){
        Log.e("load", "here");
        new ConfigTask().execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class ConfigTask extends AsyncTask<Void, Void, Void> {
        ConfigTable tempTable = null;

        @Override
        protected Void doInBackground(Void... helper){
            Log.e("doinbackground", "here");
            try{
                tempTable = new MySQLHelper().getConfig();
            }catch(Exception e){
                Log.e("Activity_config", "Setup: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("onPostExe", "here");
            Log.e("onPostExe", "Config: " + tempTable.getDesiredMoisture());
            moistureText.setText(Integer.toString(tempTable.getDesiredMoisture()));
            amountText.setText(Integer.toString(tempTable.getMinimumPumpVolume()));
            startText.setText(tempTable.getLightStart().toString());
            stopText.setText(tempTable.getLightStop().toString());
            // Set toggle button
            if (tempTable.getDisableFlood())
                toggleFlood.setActivated(true);
            else
                toggleFlood.setActivated(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
//            case R.id.action_toggle: {
//                mainMenuItem = item;
//                // Switches icons to indicate display mode vs edit mode
//                if (!toggled) {
//                    Log.e("TOGGLE?", "toggled on");
//                    item.setIcon(checkbox_on_background);
//                    toggled = true;
//                    makeEditable(true);
//                    return true;
//                } else {
//                    Log.e("TOGGLE?", "toggled off");
//                    item.setIcon(checkbox_off_background);
//                    toggled = false;
//                    makeEditable(false);
//                    return true;
//                }
//            }
            // Returns to main activity
            case android.R.id.home:{
                Log.e("Config", "Should go back/up");
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    // Swaps between display mode and edit mode.
//    protected void makeEditable(boolean enable){
//        if (!enable){
//            moistureText.setFocusable(false);
//            amountText.setFocusable(false);
//            startText.setFocusable(false);
//            stopText.setFocusable(false);
//            toggleFlood.setEnabled(false);
//            saveButton.setVisibility(View.INVISIBLE);
//        }
//        else{
//            moistureText.setFocusableInTouchMode(true);
//            amountText.setFocusableInTouchMode(true);
//            startText.setFocusableInTouchMode(true);
//            stopText.setFocusableInTouchMode(true);
//            toggleFlood.setEnabled(true);
//            saveButton.setVisibility(View.VISIBLE);
//        }
//    }

    public void onSave(View v){
        Log.e("onSAve", "gere");
        promptSave();

    }

    private class UpdateConfigTask extends AsyncTask<Void, Void, Void> {
        ConfigTable tempTable = null;

        @Override
        protected Void doInBackground(Void... helper) {
            Log.e("updateConfig", "here");
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                tempTable = new ConfigTable(Integer.parseInt(moistureText.getText().toString()),
                                            Integer.parseInt(amountText.getText().toString()),
                                            new Time(timeFormat.parse(startText.getText().toString()).getTime()),
                                            new Time((timeFormat.parse(stopText.getText().toString())).getTime()),
                                            toggleFlood.isChecked()

                );
                Log.e("UPDATE", "Stuff:\t" + tempTable.getDesiredMoisture() + "\t" + tempTable.getMinimumPumpVolume() + "\t" + tempTable.getLightStart() + "\t" + tempTable.getLightStop() + "\t" + tempTable.getDisableFlood());
                new MySQLHelper().updateConfigTable(tempTable);
            } catch (Exception e) {
                Log.e("Activity_config", "Setup: " + e);
            }

            return null;
        }
    }

    // prepares and shows a dialog to confirm deletion
    public void promptSave(){
        AlertDialog dialog = askDelete();
        dialog.show();
    }

    private AlertDialog askDelete() {
        // Creates an alert dialog that has two options
        AlertDialog.Builder deletePrompt = new AlertDialog.Builder(this);
        deletePrompt.setTitle("Save Configuration");
        deletePrompt.setMessage("Save?");

        deletePrompt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                makeEditable(false);
//                mainMenuItem.setIcon(checkbox_off_background);
                new UpdateConfigTask().execute();
                Toast.makeText(config_activity.this, "Changes saved.", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }

        });

        deletePrompt.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                loadCurrent();
                Toast.makeText(config_activity.this, "Changes reverted.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
        return deletePrompt.create();
    }
}
