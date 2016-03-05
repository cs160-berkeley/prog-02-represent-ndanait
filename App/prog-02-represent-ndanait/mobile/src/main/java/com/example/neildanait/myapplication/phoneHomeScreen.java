package com.example.neildanait.myapplication;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.text.InputFilter;
import java.util.List;
import java.util.ArrayList;

public class phoneHomeScreen extends AppCompatActivity {
    public Button autoLocationButtonClick;
    public EditText zipCode;
    public List<String> zipCodeStorage;
    public String tempZipCode;
    public int zipCodeLength;
    public Intent phoneIntent;
    public Intent watchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_home_screen);

        //Use Location Button Click Event Listener
        autoLocationButtonClick = (Button) findViewById(R.id.autoLocation); //Find Convert Button
        autoLocationButtonClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                watchIntent.putExtra("CANDIDATE_NAME", "John Doe");
                watchIntent.putExtra("PARTY_NAME", "Democrat");
                tempZipCode = "34333";
                watchIntent.putExtra("ZIP_CODE", tempZipCode);
                startService(watchIntent);

                phoneIntent = new Intent(phoneHomeScreen.this, phoneCongressionalCandidates.class);
                startActivity(phoneIntent);
            }
        });

        //Manually Enter Zip Code and Handle Enter
        zipCode = (EditText) findViewById(R.id.manualZipCode);
        zipCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipCodeLength = 5;
                zipCode.getText().clear();
                zipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(zipCodeLength)});
//                            Toast.makeText(getBaseContext(), zipCodeEntry + " was entered", Toast.LENGTH_LONG).show(); //Confirm User's Zip Code Entry
                zipCode.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            tempZipCode = zipCode.getText().toString();
                            watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                            watchIntent.putExtra("CANDIDATE_NAME", "John Doe");
                            watchIntent.putExtra("PARTY_NAME", "Democrat");
                            watchIntent.putExtra("ZIP_CODE", tempZipCode);
                            startService(watchIntent);

                            phoneIntent = new Intent(phoneHomeScreen.this, phoneCongressionalCandidates.class);
                            phoneIntent.putExtra("zipCodeEntry", tempZipCode);
                            startActivity(phoneIntent);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
