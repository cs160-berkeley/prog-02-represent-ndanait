package com.example.neildanait.myapplication;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.async.future.FutureCallback;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class phoneDetailedCandidateView extends AppCompatActivity {
    public TextView detailedCandidateName;
    public TextView detailedCandidateEndTerm;
    public String itemSelectedData;
    public ListView billsList;
    public ListView committeesList;
    public ArrayList fakeArray;
    public ArrayList fakeArray2;
    public String apiCallStringP1;
    public String apiCallStringP2;
    public String apiCallStringJoined;
    public String resultJSON;
    public String candidateCommitteesResult;
    public String candidateBillsResult;
    public JsonElement jsonElement;
    public JsonObject jsonObject;
    public JsonArray dataJSON;
    public Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedcandidate);
        Intent currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();
        if (extras != null) {
            itemSelectedData = extras.getString("ITEM");
            Log.d("23433432", itemSelectedData);
        }
        detailedCandidateName = (TextView) findViewById(R.id.detailedCandidateName);
        detailedCandidateEndTerm = (TextView) findViewById(R.id.detailedCandidateEndTerm);
        String[] tempArray = itemSelectedData.split("!!!!");
        String candidateFullName = tempArray[0];
        String candidateParty = tempArray[4];
        String candidateEnding = tempArray[5];
        String candidateBioGuideID = tempArray[6];
        String candidatePictureURL = tempArray[7];
        detailedCandidateName.setText(candidateFullName);
        detailedCandidateEndTerm.setText("Term Ends: " + candidateEnding);
        if (candidateParty.equals("D")){
            detailedCandidateName.setTextColor(Color.parseColor("#00B8FF"));
        } else {
            detailedCandidateName.setTextColor(Color.parseColor("#CE4408"));
        }
        ImageView candidatePicture = (ImageView) findViewById(R.id.detailedCandidatePicture);
        Picasso.with(context).load(candidatePictureURL).into(candidatePicture);
        Log.d("BIOGUIDE", candidateBioGuideID);
        retrieveCongressionalCandidateCommittees(candidateBioGuideID);
    }

    public void populateCommitteeAdapter(String candidateCommitteesTempResult) {
        committeesList = (ListView) findViewById(R.id.committees);
        String[] candidateCommitteesParsed = candidateCommitteesTempResult.split("!!!");
        fakeArray = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fakeArray);
        committeesList.setAdapter(arrayAdapter);
        for (String committee : candidateCommitteesParsed) {
            arrayAdapter.add(committee);
        }
    }

    public void populateBillAdapter(String candidateBillsTempResult) {
        billsList = (ListView) findViewById(R.id.bills);
        String[] candidateBillsParsed = candidateBillsTempResult.split("!!!");
        fakeArray2 = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fakeArray2);
        billsList.setAdapter(arrayAdapter);
        for (String bill : candidateBillsParsed) {
            arrayAdapter.add(bill);
        }
    }

    //Retrieve json file via bioguide ID (Use Candidate's BioGuideID)
    public void retrieveCongressionalCandidateCommittees(final String bioGuideID) {
        apiCallStringP1 = "http://congress.api.sunlightfoundation.com/committees?member_ids=";
        apiCallStringP2 = "&apikey=b50fab6ed5fc4965ab6273feea255a03";
        apiCallStringJoined = apiCallStringP1 + bioGuideID + apiCallStringP2;
        Ion.with(getBaseContext()).load(apiCallStringJoined).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                String candidateCommitteeTempResult = jsonCommitteeParser(result);
                retrieveCongressionalCandidateBills(bioGuideID, candidateCommitteeTempResult);
            }
        });
    }

    //Parse JsonObject into a data JsonArray and return final committees as a concat string
    public String jsonCommitteeParser(JsonObject json) {
        //Convert json to parsable format
        List<String> committees = new ArrayList<>();
        StringBuilder candidateCommittees = new StringBuilder();
        resultJSON = json.toString();
        jsonElement = new JsonParser().parse(resultJSON);
        jsonObject = jsonElement.getAsJsonObject();
        dataJSON = jsonObject.getAsJsonArray("results");
        for (int i = 0; i < dataJSON.size(); i++) {
            JsonElement chamberElement = dataJSON.get(i);
            JsonObject chamber = chamberElement.getAsJsonObject();
            JsonPrimitive committee = chamber.getAsJsonPrimitive("name");
            committees.add(committee.getAsString());
        }
        for (String committee : committees) {
            candidateCommittees.append(committee);
            candidateCommittees.append("!!!");
        }
        candidateCommitteesResult = candidateCommittees.toString();
        return candidateCommitteesResult;
    }

    //Retrieve json file via bioguide ID (Use Candidate's BioGuideID)
    public void retrieveCongressionalCandidateBills(String bioGuideID, final String candidateCommitteesTempResult) {
        apiCallStringP1 = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=";
        apiCallStringP2 = "&apikey=b50fab6ed5fc4965ab6273feea255a03";
        apiCallStringJoined = apiCallStringP1 + bioGuideID + apiCallStringP2;
        Ion.with(getBaseContext()).load(apiCallStringJoined).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                populateCommitteeAdapter(candidateCommitteesTempResult);
                String candidateBillsTempResult = jsonBillParser(result);
                populateBillAdapter(candidateBillsTempResult);
            }
        });
    }

    //Parse JsonObject into a data JsonArray and return final bills as a concat string
    public String jsonBillParser(JsonObject json) {
    //Convert json to parsable format
        List<String> bills = new ArrayList<>();
        StringBuilder candidateBills = new StringBuilder();
        resultJSON = json.toString();
        jsonElement = new JsonParser().parse(resultJSON);
        jsonObject = jsonElement.getAsJsonObject();
        dataJSON = jsonObject.getAsJsonArray("results");
        for (int i = 0; i < dataJSON.size(); i++) {
            JsonElement billElement = dataJSON.get(i);
            JsonObject bill = billElement.getAsJsonObject();
            JsonElement billName = bill.get("short_title");
            if (!(billName.toString().equals("null"))) {
                bills.add(billName.toString());
            }
        }
        for (String bill : bills) {
            candidateBills.append(bill);
            candidateBills.append("!!!");
        }
        candidateBillsResult = candidateBills.toString();
        return candidateBillsResult;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phone_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
