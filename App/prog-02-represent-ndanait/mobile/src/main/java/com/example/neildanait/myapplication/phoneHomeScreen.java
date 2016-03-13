package com.example.neildanait.myapplication;
import android.location.Address;
import android.location.Geocoder;
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
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class phoneHomeScreen extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener{
    public Button autoLocationButtonClick;
    public EditText zipCode;
    public String tempZipCode;
    public String mLatitudeText;
    public String mLongitudeText;
    public String candidatePostalCode;
    public String candidateCounty;
    public String resultJSON;
    public int zipCodeLength;
    public Intent phoneIntent;
    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    public JsonElement jsonElement;
    public JsonObject jsonObject;
    public JsonArray dataJSON;
    protected static final String TAG = "basic-location-sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_home_screen);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    //Sets the click listeners for location
    public void setLocationClickListener(String county, String postalCode) {
        //Use Location Button Click Event Listener
        candidateCounty = county;
        candidatePostalCode = postalCode;
        autoLocationButtonClick = (Button) findViewById(R.id.autoLocation); //Find Convert Button
        autoLocationButtonClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                phoneIntent = new Intent(phoneHomeScreen.this, phoneCongressionalCandidates.class);
                if (mLatitudeText != null && mLongitudeText != null) {
                    phoneIntent.putExtra("LATITUDE", mLatitudeText);
                    phoneIntent.putExtra("LONGITUDE", mLongitudeText);
                    phoneIntent.putExtra("ZIP_CODE", "Doesn't Exist");
                    phoneIntent.putExtra("POSTAL_CODE", candidatePostalCode);
                    phoneIntent.putExtra("COUNTY_NAME", candidateCounty);
                }
                startActivity(phoneIntent);
            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        }
        getPostalCodeFromLatLon(mLatitudeText, mLongitudeText);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    //Retrieves zipcode value of latitude and longitude
    public void getPostalCodeFromLatLon(String mLatitudeText, String mLongitudeText) {
        Geocoder geocoder = new Geocoder(this);
        Address address = new Address(Locale.getDefault());
        String postalCode;
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(mLatitudeText), Double.parseDouble(mLongitudeText), 1);
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        postalCode = address.getPostalCode();
        getCountyFromAddress(address, postalCode);
        setZipCodeLocationListener();
    }

    //Retrieves county value associated with address
    public void getCountyFromAddress(Address address, String postalCode) {
        String addressLine1 = address.getAddressLine(0).toString().trim().replaceAll(" ", "");
        String addressLine2 = address.getAddressLine(1).toString().trim().replaceAll(" ", "");
        String countyAPICallStringP1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String countyAPICallStringP2 = "&key=AIzaSyANJNctP_j48LouappAerd664zhtc-kotE";
        String countyAPICallJoined = countyAPICallStringP1 + addressLine1 + "," + addressLine2 + countyAPICallStringP2;
        final String postalCodeFinal = postalCode;
        Ion.with(getBaseContext()).load(countyAPICallJoined).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                resultJSON = result.toString();
                jsonElement = new JsonParser().parse(resultJSON);
                jsonObject = jsonElement.getAsJsonObject();
                dataJSON = jsonObject.getAsJsonArray("results");
                JsonElement addressComponent = dataJSON.get(0);
                JsonObject addressComponentObject = addressComponent.getAsJsonObject();
                JsonArray addressComponentArray = addressComponentObject.getAsJsonArray("address_components");
                JsonElement countyComponent = addressComponentArray.get(4);
                JsonObject countyComponentObject = countyComponent.getAsJsonObject();
                JsonElement countyElement = countyComponentObject.get("short_name");
                final String countyFinal = countyElement.getAsString();

                //Set all listeners now that values have been attained
                setLocationClickListener(countyFinal, postalCodeFinal);
            }
        });
    }

    //Sets the click listeners for zip code
    public void setZipCodeLocationListener() {
        //Manually Enter Zip Code and Handle Enter
        zipCode = (EditText) findViewById(R.id.manualZipCode);
        zipCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipCodeLength = 5;
                zipCode.getText().clear();
                zipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(zipCodeLength)});
                zipCode.setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            tempZipCode = zipCode.getText().toString();
                            getCountyFromZip(tempZipCode);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    //Retrieves county value associated with zip
    public void getCountyFromZip(String postalCode) {
        String countyAPICallStringP1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String countyAPICallStringP2 = "&key=AIzaSyANJNctP_j48LouappAerd664zhtc-kotE";
        String countyAPICallJoined = countyAPICallStringP1 + postalCode + countyAPICallStringP2;
        final String postalCodeFinal = postalCode;
        Ion.with(getBaseContext()).load(countyAPICallJoined).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                resultJSON = result.toString();
                jsonElement = new JsonParser().parse(resultJSON);
                jsonObject = jsonElement.getAsJsonObject();
                dataJSON = jsonObject.getAsJsonArray("results");
                JsonElement addressComponent = dataJSON.get(0);
                JsonObject addressComponentObject = addressComponent.getAsJsonObject();
                JsonArray addressComponentArray = addressComponentObject.getAsJsonArray("address_components");
                JsonElement countyComponent = addressComponentArray.get(2);
                JsonObject countyComponentObject = countyComponent.getAsJsonObject();
                JsonElement countyElement = countyComponentObject.get("short_name");
                final String countyFinal = countyElement.getAsString();
                phoneIntent = new Intent(phoneHomeScreen.this, phoneCongressionalCandidates.class);
                phoneIntent.putExtra("LATITUDE", "Doesn't Exist");
                phoneIntent.putExtra("LONGITUDE", "Doesn't Exist");
                phoneIntent.putExtra("ZIP_CODE", postalCodeFinal);
                phoneIntent.putExtra("POSTAL_CODE", postalCodeFinal);
                phoneIntent.putExtra("COUNTY_NAME", countyFinal);
                startActivity(phoneIntent);
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
