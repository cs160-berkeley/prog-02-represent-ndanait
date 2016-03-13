package com.example.neildanait.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

import com.squareup.picasso.Picasso;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    //Credits to StackOverFlow Assistance (customlistadapter with texts and images)
    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);
        TextView candidateName = (TextView) rowView.findViewById(R.id.candidateName);
        ImageView candidatePicture = (ImageView) rowView.findViewById(R.id.candidatePicture);
        TextView candidateDescription1 = (TextView) rowView.findViewById(R.id.candidateDescription1);
        TextView candidateDescription2 = (TextView) rowView.findViewById(R.id.candidateDescription2);
        if (itemname[position] != null) {
            String temp = itemname[position];
            String[] tempArray = temp.split("!!!!");
            String candidateFullName = tempArray[0];
            String candidateEmail = tempArray[1];
            String candidateWebsite = tempArray[2];
            String candidateTweet = tempArray[3];
            String candidateParty = tempArray[4];
            String candidateTwitterImageURL = tempArray[7];

            if (candidateParty.equals("D")){
                candidateName.setTextColor(Color.parseColor("#00B8FF"));
            } else if (candidateParty.equals("R")){
                candidateName.setTextColor(Color.parseColor("#CE4408"));
            } else {
                candidateName.setTextColor(Color.parseColor("#66CD00"));
            }

            candidateName.setText(candidateFullName);
            Picasso.with(context).load(candidateTwitterImageURL).into(candidatePicture);
            candidateDescription1.setText(candidateEmail + "\n" + candidateWebsite + "\n");

            //Hyperlink the TextView
//            candidateDescription1.setLinksClickable(true);
//            Linkify.addLinks(candidateDescription1, Linkify.ALL);

            candidateDescription1.setTextColor(Color.parseColor("#5d5d5d"));
            candidateDescription2.setText("\n" + "'" + candidateTweet + "'");
            candidateDescription2.setTextColor(Color.parseColor("#5d5d5d"));
        }
        return rowView;
    }
}
