package com.example.neildanait.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class watchElection extends Activity {
    public String zipCodeEntry;
    public TextView obamaResults;
    public TextView romneyResults;
    public Integer obamaResultsInt;
    public Integer romneyResultsInt;
    public Integer zipCodeEntryInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electionresults);
        Intent currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();

        //Randomize the values based on zipCode
        if (extras != null) {
            zipCodeEntry = extras.getString("ZIP_CODE");
        }
        if (zipCodeEntry != null) {
            zipCodeEntryInt = Integer.parseInt(zipCodeEntry);
            obamaResults = (TextView) findViewById(R.id.obamaresults);
            romneyResults = (TextView) findViewById(R.id.romneyresults);
            obamaResultsInt = zipCodeEntryInt % 100;
            String obamaText = Integer.toString(obamaResultsInt) + "%";
            obamaResults.setText(obamaText);
            romneyResultsInt = 100 - obamaResultsInt;
            String romneyText = Integer.toString(romneyResultsInt) +"%";
            romneyResults.setText(romneyText);
        }
    }
}
