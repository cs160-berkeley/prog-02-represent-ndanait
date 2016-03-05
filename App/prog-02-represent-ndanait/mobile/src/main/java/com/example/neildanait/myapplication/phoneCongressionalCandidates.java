package com.example.neildanait.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.Set;

public class phoneCongressionalCandidates extends AppCompatActivity {
    public ListView senators, representatives;
    public String[] senatorNameData = {"John Doe", "Jake Dawg"};
    public String[] representativeNameData = {"James Dude"};
    public Integer[] senatorImageData = {R.drawable.senator1, R.drawable.senator2};
    public Integer[] representativeImageData = {R.drawable.rep1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congressionalcandidates);
        senators = (ListView) findViewById(R.id.senators);
        representatives = (ListView) findViewById(R.id.representatives);
        CustomListAdapter adapter1 = new CustomListAdapter(this, senatorNameData, senatorImageData);
        CustomListAdapter adapter2 = new CustomListAdapter(this, representativeNameData, representativeImageData);
        senators.setAdapter(adapter1);
        representatives.setAdapter(adapter2);
        ListUtils.setDynamicHeight(senators);
        ListUtils.setDynamicHeight(representatives);
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(phoneCongressionalCandidates.this, phoneDetailedCandidateView.class));
            }
        };
        senators.setOnItemClickListener(itemClickListener);
        representatives.setOnItemClickListener(itemClickListener); // Will not bring a representative yet
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
