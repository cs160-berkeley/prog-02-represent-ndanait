package com.example.neildanait.myapplication;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.async.future.FutureCallback;

import twitter4j.Twitter;
import twitter4j.*;
import twitter4j.Status;
import twitter4j.conf.*;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class phoneCongressionalCandidates extends AppCompatActivity implements ThreadCompleteListener {
    public ListView senators, representatives;
    public String[] senatorInformation;
    public String[] representativeInformation;
    public String[] senatorInformationEdited;
    public String[] representativeInformationEdited;
    public String[] allInformation;
    public String[] allCandidateNames;
    public String[] allCandidateParty;
    public Integer[] senatorImageData = {R.drawable.senator2}; //ignore hardcoded values
    public Integer[] representativeImageData = {R.drawable.rep1}; //ignore hardcoded values
    public String mLatitudeText;
    public String mLongitudeText;
    public String zipCodeEntry;
    public String candidatePostalCode;
    public String apiCallStringP1;
    public String apiCallStringP2;
    public String apiCallStringP3;
    public String apiCallStringJoined;
    public String resultJSON;
    public String candidateTweet;
    public String candidateTwitterImageURL;
    public String candidateCounty;
    public JsonElement jsonElement;
    public JsonObject jsonObject;
    public JsonArray dataJSON;
    public StringBuilder fullCandidateName;
    public JsonPrimitive candidateEmail;
    public JsonPrimitive candidateWebsite;
    public JsonPrimitive candidateType;
    public JsonPrimitive candidateParty;
    public JsonPrimitive candidateEnding;
    public JsonPrimitive candidateBioGuideID;
    public Intent currentIntent;
    public Intent watchIntent;
    public int a;
    public int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congressionalcandidates);
        watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();
        if (extras != null) {
            mLatitudeText = extras.getString("LATITUDE");
            mLongitudeText = extras.getString("LONGITUDE");
            zipCodeEntry = extras.getString("ZIP_CODE");
            candidatePostalCode = extras.getString("POSTAL_CODE");
            candidateCounty = extras.getString("COUNTY_NAME");
        }
        retrieveCongressionalCandidates(mLatitudeText, mLongitudeText, zipCodeEntry);
    }

    //Retrieve json file via latitude and longitude (Use Location) or zipCodeEntry (Enter Zip Code)
    public void retrieveCongressionalCandidates(String latitude, String longitude, String zipCodeEntry) {
        if (zipCodeEntry.equals("Doesn't Exist")) {
            apiCallStringP1 = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=";
            apiCallStringP2 = "&longitude=";
            apiCallStringP3 = "&apikey=b50fab6ed5fc4965ab6273feea255a03";
            apiCallStringJoined = apiCallStringP1 + latitude + apiCallStringP2 + longitude + apiCallStringP3;
            Ion.with(getBaseContext()).load(apiCallStringJoined).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    jsonBasicInfoParser(jsonParser(result));
                }
            });
        } else {
            apiCallStringP1 = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=";
            apiCallStringP2 = "&apikey=b50fab6ed5fc4965ab6273feea255a03";
            apiCallStringJoined = apiCallStringP1 + zipCodeEntry + apiCallStringP2;
            Ion.with(getBaseContext()).load(apiCallStringJoined).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    jsonBasicInfoParser(jsonParser(result));
                }
            });
        }
    }
    //Parse JsonObject into a data JsonArray
    public JsonArray jsonParser(JsonObject json) {
        //Convert json to parsable format
        resultJSON = json.toString();
        jsonElement = new JsonParser().parse(resultJSON);
        jsonObject = jsonElement.getAsJsonObject();
        dataJSON = jsonObject.getAsJsonArray("results");
        return dataJSON;
    }

    //Parses data JsonArray and collects all basic info for each candidate
    public void jsonBasicInfoParser(JsonArray data) {
        //Retrieve specific json data elements
        senatorInformation = new String[2];
        representativeInformation = new String[2];
        a = 0;
        b = 0;
        //Start a Twitter Thread
        NotifyingThread twitterThread = new NotifyingThread() {
            @Override
            public void doRun() {
                allInformation = new String[dataJSON.size()];
                allCandidateNames = new String[dataJSON.size()];
                allCandidateParty = new String[dataJSON.size()];
                for (int i = 0; i < dataJSON.size(); i++) {
                    JsonElement candidateElement = dataJSON.get(i);
                    JsonObject candidate = candidateElement.getAsJsonObject();
                    JsonPrimitive candidateFirstName = candidate.getAsJsonPrimitive("first_name");
                    JsonPrimitive candidateLastName = candidate.getAsJsonPrimitive("last_name");

                    fullCandidateName = new StringBuilder();
                    fullCandidateName.append(candidateFirstName.getAsString());
                    fullCandidateName.append(" ");
                    fullCandidateName.append(candidateLastName.getAsString());

                    candidateEmail = candidate.getAsJsonPrimitive("oc_email");
                    candidateWebsite = candidate.getAsJsonPrimitive("website");
                    candidateType = candidate.getAsJsonPrimitive("chamber");
                    candidateParty = candidate.getAsJsonPrimitive("party");
                    candidateEnding = candidate.getAsJsonPrimitive("term_end");
                    candidateBioGuideID = candidate.getAsJsonPrimitive("bioguide_id");
                    final JsonPrimitive candidateTwitterID = candidate.getAsJsonPrimitive("twitter_id");

                    String[] twitterData = getLastTweetPlusImage(candidateTwitterID.getAsString());
                    candidateTweet = twitterData[0];
                    candidateTwitterImageURL = twitterData[1];

                    String fullCandidateInformation = fullCandidateName.toString() + "!!!!" + candidateEmail.getAsString() + "!!!!" + candidateWebsite.getAsString() + "!!!!" + candidateTweet + "!!!!" + candidateParty.getAsString() + "!!!!" + candidateEnding.getAsString() + "!!!!" + candidateBioGuideID.getAsString() + "!!!!" + candidateTwitterImageURL;
                    allCandidateNames[i] = fullCandidateName.toString();
                    allCandidateParty[i] = candidateParty.getAsString();
                    allInformation[i] = fullCandidateInformation;
                }
            }
        };
        twitterThread.addListener(this);
        twitterThread.start();
    }

    //Retrieves last tweet and image for a particular candidate's twitterID using the User class
    public String[] getLastTweetPlusImage(String twitterID) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("sb52UFykkwSwbQihDEVDPCdPV");
        cb.setOAuthConsumerSecret("Me35mCXKfS67VFK9kUFL9aJNBn5xbDSL7M5GrZxx0it0PqDom7");
        cb.setOAuthAccessToken("708221428512862208-NHmx62uqUkvNmkzAvLQuP0IE5aFaaTA");
        cb.setOAuthAccessTokenSecret("1RhgBQLcw2evfQfW0WiohaueFwEOxuRnY4uLdLFPb5FQt");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            User user = twitter.showUser(twitterID);
            Status status = user.getStatus();
            candidateTweet = status.getText();
            candidateTwitterImageURL = user.getOriginalProfileImageURL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] twitterData = {candidateTweet, candidateTwitterImageURL};
        return twitterData;
    }

    public void notifyOfThreadComplete(final Thread thread){
        StringBuilder names = new StringBuilder();
        StringBuilder parties = new StringBuilder();
        for (String info : allInformation) {
            if (info.contains("Rep")) {
                representativeInformation[b] = info;
                b++;
            } else {
                senatorInformation[a] = info;
                a++;
            }
        }
        for (int i=0; i < allCandidateNames.length; i++) {
            names.append("\n" + allCandidateNames[i]);
            parties.append("\n" + allCandidateParty[i]);
        }

        //Generate watch intents with these values
        watchIntent.putExtra("CANDIDATE_NAME", names.toString());
        watchIntent.putExtra("PARTY_NAME", parties.toString());
        watchIntent.putExtra("POSTAL_CODE", candidatePostalCode);
        watchIntent.putExtra("COUNTY_NAME", candidateCounty);
        startService(watchIntent); //Bring Up Candidates on Watch

        senators = (ListView) findViewById(R.id.senators);
        representatives = (ListView) findViewById(R.id.representatives);

        //Check to make sure the senator data has no null elements
        if (senatorInformation[senatorInformation.length - 1] == null) {
            senatorInformationEdited = new String[senatorInformation.length -1];
            for (int i=0; i < senatorInformation.length - 1; i++) {
                senatorInformationEdited[i] = senatorInformation[i];
            }
        } else {
            senatorInformationEdited = new String[senatorInformation.length];
            for (int i=0; i < senatorInformation.length; i++) {
                senatorInformationEdited[i] = senatorInformation[i];
            }
        }

        //Check to make sure the rep data has no null elements
        if (representativeInformation[representativeInformation.length - 1] == null) {
            representativeInformationEdited = new String[representativeInformation.length -1];
            for (int i=0; i < representativeInformation.length - 1; i++) {
                representativeInformationEdited[i] = representativeInformation[i];
            }
        } else {
            representativeInformationEdited = new String[representativeInformation.length];
            for (int i=0; i < representativeInformation.length; i++) {
                representativeInformationEdited[i] = representativeInformation[i];
            }
        }

        //Create adapters and set them
        final CustomListAdapter adapter1 = new CustomListAdapter(this, senatorInformationEdited, senatorImageData);
        final CustomListAdapter adapter2 = new CustomListAdapter(this, representativeInformationEdited, representativeImageData);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                senators.setAdapter(adapter1);
                representatives.setAdapter(adapter2);
                ListUtils.setDynamicHeight(senators);
                ListUtils.setDynamicHeight(representatives);
                OnItemClickListener itemClickListener1 = new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object item = senators.getItemAtPosition(position);
                        String itemString = String.valueOf(item);
                        Intent detailedIntent = new Intent(phoneCongressionalCandidates.this, phoneDetailedCandidateView.class);
                        detailedIntent.putExtra("ITEM", itemString);
                        startActivity(detailedIntent);
                    }
                };
                OnItemClickListener itemClickListener2 = new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object item = representatives.getItemAtPosition(position);
                        String itemString = String.valueOf(item);
                        Intent detailedIntent = new Intent(phoneCongressionalCandidates.this, phoneDetailedCandidateView.class);
                        detailedIntent.putExtra("ITEM", itemString);
                        startActivity(detailedIntent);
                    }
                };
                senators.setOnItemClickListener(itemClickListener1);
                representatives.setOnItemClickListener(itemClickListener2);
            }
        });
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