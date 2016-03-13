package com.example.neildanait.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class watchElection extends Activity {
    public String candidatePostalCode;
    public String candidateCounty;
    public String sensorCounty;
    public TextView obamaResults;
    public TextView romneyResults;
    public TextView candidateCountyView;
    public String obamaPercent;
    public String romneyPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electionresults);
        Intent currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();

        //Randomize the values based on zipCode
        if (extras != null) {
            candidatePostalCode = extras.getString("POSTAL_CODE");
            candidateCounty = extras.getString("COUNTY_NAME");
            sensorCounty = extras.getString("RANDOM");
        }
        obamaResults = (TextView) findViewById(R.id.obamaresults);
        romneyResults = (TextView) findViewById(R.id.romneyresults);
        candidateCountyView = (TextView) findViewById(R.id.county);
        candidateCountyView.setText(candidateCounty);

        if (sensorCounty == null) {
//            Log.d("Nice", "It came here");
//            Log.d("Nice", candidateCounty);
            populateVoteData(candidateCounty);
            candidateCountyView.setText(candidateCounty);
        } else if (sensorCounty != null) {
//            Log.d("Nice", "It came here actually");
//            Log.d("Nice", sensorCounty);
            populateVoteData(sensorCounty);
            candidateCountyView.setText(sensorCounty);
        } else {
//            Log.d("Nice", "It came here actually yay");
//            Log.d("Nice", candidateCounty);
            populateVoteData(candidateCounty);
            candidateCountyView.setText(candidateCounty);
        }
    }

    public void populateVoteData(String county) {
        try {
            InputStream votingData = watchElection.this.getResources().openRawResource(R.raw.election_county_2012);
            BufferedReader votingDataReader = new BufferedReader (new InputStreamReader(votingData));
            StringBuilder stringBuilder = new StringBuilder();

            for (String line = null; (line = votingDataReader.readLine()) != null;) {
                stringBuilder.append(line).append("\n");
            }
            String result = stringBuilder.toString();
            JSONTokener tokener = new JSONTokener(result);
            JSONArray voteData = new JSONArray(tokener);

            for (int i = 0; i < voteData.length(); i++) {
                JSONObject countyData = (JSONObject) voteData.get(i);
                StringBuilder fullCountyName = new StringBuilder();
                fullCountyName.append(countyData.get("county-name"));
                fullCountyName.append(" County");
                if (fullCountyName.toString().contains(county)) {
                    obamaPercent = countyData.get("obama-percentage").toString();
                    romneyPercent = countyData.get("romney-percentage").toString();
                    obamaResults.setText(obamaPercent + "%");
                    romneyResults.setText(romneyPercent + "%");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
