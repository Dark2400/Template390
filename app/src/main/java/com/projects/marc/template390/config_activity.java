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


public class config_activity extends AppCompatActivity {

    protected static Toolbar myToolbar;
    protected static Button saveButton;
    protected static EditText moistureText;
    protected static EditText amountText;
    protected static EditText startText;
    protected static EditText stopText;
    protected static ToggleButton toggleFlood;
    protected static MenuItem mainMenuItem = null;
    protected static ConfigTable currentTable = null;
    protected static MySQLHelper helper = null;
    protected static boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_activity);
        setupUI();
    }

    protected void onStart() {
        super.onStart();
        loadCurrent();
    }


    protected void setupUI() {
        Log.e("setup", "here");
        saveButton = (Button) findViewById(R.id.saveButton);
        moistureText = (EditText) findViewById(R.id.moistureInput);
        amountText = (EditText) findViewById(R.id.amountInput);
        startText = (EditText) findViewById(R.id.startInput);
        stopText = (EditText) findViewById(R.id.stopInput);
        toggleFlood = (ToggleButton) findViewById(R.id.floodButton);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

    }

    protected void loadCurrent() {
//        Log.e("load", "here");
        new ConfigTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Returns to main activity
            case android.R.id.home: {
//                Log.e("Config", "Should go back/up");
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onSave(View v) {
        Log.e("onSAve", "gere");
        promptSave();
    }

    // prepares and shows a dialog to confirm deletion
    public void promptSave() {
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
                new UpdateConfigTask().execute();
                if (result)
                    Toast.makeText(config_activity.this, "Changes saved.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(config_activity.this, "Files not saved, error in attempting to save.", Toast.LENGTH_SHORT).show();

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

    private class UpdateConfigTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... helper) {
//            Log.e("updateConfig", "here");
            if (helper != null) {
                if (checkMinLength()) {
                    try {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        currentTable = new ConfigTable(Integer.parseInt(moistureText.getText().toString()),
                                Integer.parseInt(amountText.getText().toString()),
                                new Time(timeFormat.parse(startText.getText().toString()).getTime()),
                                new Time((timeFormat.parse(stopText.getText().toString())).getTime()),
                                toggleFlood.isChecked()

                        );
                        //                    Log.e("UPDATE", "Stuff:\t" + currentTable.getDesiredMoisture() + "\t" + currentTable.getMinimumPumpVolume() + "\t" + currentTable.getLightStart() + "\t" + currentTable.getLightStop() + "\t" + currentTable.getDisableFlood());
                        new MySQLHelper().updateConfigTable(currentTable);
                    } catch (Exception e) {
                        //                    Log.e("Activity_config", "Setup: " + e);
                    }
                    result = true;
                }
            }
            result = false;
            return null;
        }
    }

    public boolean checkMinLength() {
        if (moistureText.getText().toString().length() > 0 &&
                amountText.getText().toString().length() > 0 &&
                startText.getText().toString().length() > 0 &&
                stopText.getText().toString().length() > 0
                )
            return true;
        return false;
    }

    private class ConfigTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... something) {
            Log.e("doinbackground", "here");
            try {
                helper = new MySQLHelper();
                if (helper != null)
                    currentTable = helper.getConfig();
            } catch (Exception e) {
                Log.e("Activity_config", "Setup: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (helper != null) {
//                Log.e("onPostExe", "here");
//                Log.e("onPostExe", "Config: " + currentTable.getDesiredMoisture());
                moistureText.setText(Integer.toString(currentTable.getDesiredMoisture()));
                amountText.setText(Integer.toString(currentTable.getMinimumPumpVolume()));
                startText.setText(currentTable.getLightStart().toString());
                stopText.setText(currentTable.getLightStop().toString());
                // Set toggle button
                if (currentTable.getDisableFlood())
                    toggleFlood.setActivated(true);
                else
                    toggleFlood.setActivated(false);
            } else {
                moistureText.setText("");
                amountText.setText("");
                startText.setText("");
                stopText.setText("");
                toggleFlood.setActivated(false);
                Toast.makeText(config_activity.this, "Unable to reach MySQL server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



