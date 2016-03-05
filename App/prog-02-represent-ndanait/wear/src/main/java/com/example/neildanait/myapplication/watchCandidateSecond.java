package com.example.neildanait.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class watchCandidateSecond extends Activity {

    private TextView candidateInfo;
    public Button detailedView;
    public Intent watchIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchsecondcandidate);
        detailedView = (Button) findViewById(R.id.detailedView);

        detailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                startService(sendIntent);

                watchIntent = new Intent(watchCandidateSecond.this, watchElection.class);
                startActivity(watchIntent);
            }
        });
        addListenerOnImage();
    }

    //StackOverFlowCredits
    public void addListenerOnImage() {
        candidateInfo = (TextView) findViewById(R.id.candidateInfo);
        candidateInfo.setOnTouchListener(new OnSwipeTouchListener(watchCandidateSecond.this) {
            @Override
            public void onSwipeTop() {
                Toast.makeText(watchCandidateSecond.this, "Top Swipe", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), watchCandidateFirst.class);
                startActivity(intent);
            }

            public void onSwipeRight() {
                Toast.makeText(watchCandidateSecond.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(watchCandidateSecond.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(watchCandidateSecond.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
