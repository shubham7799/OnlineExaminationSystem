package com.example.onlineexaminationsystem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ResultAdapter extends ArrayAdapter {
    Context context;
    ArrayList<String> name;
    ArrayList<Integer> score;
    ArrayList<StudentRecord> recordArrayList;
    public ResultAdapter(@NonNull Context context, ArrayList<String> name,ArrayList<Integer> score) {
        super(context,0, name);
        this.context=context;
        this.name=name;
        this.score=score;
    }
    public ResultAdapter(@NonNull Context context, ArrayList<StudentRecord> recordArrayList) {
        super(context,0, recordArrayList);
        this.context=context;
        this.recordArrayList=recordArrayList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.result_layout,parent,false);
        if (name==null){
            StudentRecord r=recordArrayList.get(position);
            ((TextView)convertView.findViewById(R.id.resultName)).setText(r.getExamname());
            ((TextView)convertView.findViewById(R.id.resultScore)).setText("Score: "+r.getScore());
            convertView.findViewById(R.id.resultdetail).setVisibility(View.VISIBLE);
            ((TextView)convertView.findViewById(R.id.resultspecs)).setText(r.getSubject()+" ("+r.getStream()+")");
        }
        else {
            ((TextView)convertView.findViewById(R.id.resultName)).setText(name.get(position));
            ((TextView)convertView.findViewById(R.id.resultScore)).setText("Score: "+score.get(position));
            Log.v("firebase","done");
        }
        return convertView;
    }
}
