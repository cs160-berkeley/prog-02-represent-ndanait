package com.example.neildanait.myapplication;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import java.nio.charset.StandardCharsets;

public class WatchListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

        String[] tempArray = value.split("\n");
        String zipCodeEntry = tempArray[1];
        Intent intent = new Intent(this, watchCandidateFirst.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("CANDIDATE_NAME", "John Doe");
        intent.putExtra("PARTY_NAME", "Democrat");
        intent.putExtra("ZIP_CODE", zipCodeEntry);
        startActivity(intent);
        super.onMessageReceived(messageEvent);
    }
}