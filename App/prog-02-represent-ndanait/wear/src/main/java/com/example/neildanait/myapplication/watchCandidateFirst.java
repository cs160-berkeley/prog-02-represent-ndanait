package com.example.neildanait.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class watchCandidateFirst extends Activity {

    private TextView candidateInfo;
    public Button detailedView;
    public Intent watchIntent;
    public String zipCodeEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchfirstcandidate);
        detailedView = (Button) findViewById(R.id.detailedView);
        detailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                startService(sendIntent);

                watchIntent = new Intent(watchCandidateFirst.this, watchElection.class);
                Intent currentIntent = getIntent();
                Bundle extras = currentIntent.getExtras();
                if (extras != null) {
                    zipCodeEntry = extras.getString("ZIP_CODE");
                    watchIntent.putExtra("ZIP_CODE", zipCodeEntry);
                }
                startActivity(watchIntent);
            }
        });
        addListenerOnImage();
    }

    //StackOverFlowCredits
    public void addListenerOnImage() {
        candidateInfo = (TextView) findViewById(R.id.candidateInfo);
        candidateInfo.setOnTouchListener(new OnSwipeTouchListener(watchCandidateFirst.this) {
            @Override
            public void onSwipeTop() {
                Toast.makeText(watchCandidateFirst.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(watchCandidateFirst.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(watchCandidateFirst.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(watchCandidateFirst.this, "Bottom Swipe", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), watchCandidateSecond.class);
                startActivity(intent);
            }
        });
    }
}
