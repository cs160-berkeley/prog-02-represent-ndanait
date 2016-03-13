package com.example.neildanait.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class watchCandidateFirst extends Activity {

    public TextView candidateInfo;
    public Button detailedView;
    public Intent watchIntent;
    public Intent currentIntent;
    public String candidateName;
    public String candidateParty;
    public String candidatePostalCode;
    public String candidateCounty;
    public String[] candidateNamesParsed;
    public String[] candidatePartiesParsed;
    public int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchfirstcandidate);
        i = 0;
        currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();
        if (extras != null) {
            candidateName = extras.getString("CANDIDATE_NAME");
            candidateParty = extras.getString("PARTY_NAME");
            candidatePostalCode = extras.getString("POSTAL_CODE");
            candidateCounty = extras.getString("COUNTY_NAME");
        }

        candidateInfo = (TextView) findViewById(R.id.candidateInfo);
        candidateNamesParsed = candidateName.split("\n");
        candidatePartiesParsed = candidateParty.split("\n");
        candidateInfo.setText(candidateNamesParsed[i+1] + "\n" + "(" +  candidatePartiesParsed[i+1] + ")");
        if (candidatePartiesParsed[i+1].equals("D")){
            candidateInfo.setTextColor(Color.parseColor("#00B8FF"));
        } else if (candidatePartiesParsed[i+1].equals("R")){
            candidateInfo.setTextColor(Color.parseColor("#CE4408"));
        } else {
            candidateInfo.setTextColor(Color.parseColor("#66CD00"));
        }

        detailedView = (Button) findViewById(R.id.detailedView);
        detailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                startService(sendIntent);
                watchIntent = new Intent(watchCandidateFirst.this, watchElection.class);
                watchIntent.putExtra("POSTAL_CODE", candidatePostalCode);
                watchIntent.putExtra("COUNTY_NAME", candidateCounty);
                startActivity(watchIntent);
            }
        });
        addListenerOnImage();
    }

    //StackOverFlowCredits
    public void addListenerOnImage() {
        candidateInfo.setOnTouchListener(new OnSwipeTouchListener(watchCandidateFirst.this) {
            @Override
            public void onSwipeTop() {
                Toast.makeText(watchCandidateFirst.this, "top", Toast.LENGTH_SHORT).show();
                if (i >= 1) {
                    i--;
                    candidateInfo.setText(candidateNamesParsed[i] + "\n" + "(" + candidatePartiesParsed[i] + ")");
                    if (candidatePartiesParsed[i].equals("D")){
                        candidateInfo.setTextColor(Color.parseColor("#00B8FF"));
                    } else if (candidatePartiesParsed[i].equals("R")){
                        candidateInfo.setTextColor(Color.parseColor("#CE4408"));
                    } else {
                        candidateInfo.setTextColor(Color.parseColor("#66CD00"));
                    }
                }
            }

            public void onSwipeRight() {
                Toast.makeText(watchCandidateFirst.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(watchCandidateFirst.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(watchCandidateFirst.this, "Bottom Swipe", Toast.LENGTH_SHORT).show();
                if (i < candidateNamesParsed.length) {
                    i++;
                    candidateInfo.setText(candidateNamesParsed[i] + "\n" + "(" +  candidatePartiesParsed[i] + ")");
                    if (candidatePartiesParsed[i].equals("D")){
                        candidateInfo.setTextColor(Color.parseColor("#00B8FF"));
                    } else if (candidatePartiesParsed[i].equals("R")){
                        candidateInfo.setTextColor(Color.parseColor("#CE4408"));
                    } else {
                        candidateInfo.setTextColor(Color.parseColor("#66CD00"));
                    }
                }
            }
        });
    }
}
