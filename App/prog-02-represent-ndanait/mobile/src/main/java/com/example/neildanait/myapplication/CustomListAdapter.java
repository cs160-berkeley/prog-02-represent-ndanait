package com.example.neildanait.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
    public List republicanCandidates;
    public List democraticCandidates;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null, true);
        TextView candidateName = (TextView) rowView.findViewById(R.id.candidateName);
        ImageView candidatePicture = (ImageView) rowView.findViewById(R.id.candidatePicture);
        TextView candidateDescription = (TextView) rowView.findViewById(R.id.candidateDescription);

        candidateName.setText(itemname[position]);
        republicanCandidates = new ArrayList();
        democraticCandidates = new ArrayList();
        republicanCandidates.add("Jake Dawg");
        democraticCandidates.add("John Doe");
        democraticCandidates.add("James Dude");
        if (republicanCandidates.contains(itemname[position])) {
            candidateName.setTextColor(Color.parseColor("#CE4408"));
        } else {
            candidateName.setTextColor(Color.parseColor("#00B8FF"));
        }
        candidatePicture.setImageResource(imgid[position]);
        candidateDescription.setText(
                "firstlast@gmail.com\n" +
                "www.firstlast.org\n" +
                "“Thank you for all your support, State! It is a new American century.”"
        );
        candidateDescription.setTextColor(Color.parseColor("#5d5d5d"));
        return rowView;
    };
}
