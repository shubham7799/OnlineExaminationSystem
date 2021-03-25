package com.example.onlineexaminationsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.onlineexaminationsystem.HomeActivity.ROLE;
import static com.example.onlineexaminationsystem.HomeActivity.USER;
import static com.example.onlineexaminationsystem.HomeActivity.getStream;
import static com.example.onlineexaminationsystem.Subject.STREAM;
import static com.example.onlineexaminationsystem.Subject.SUBJECT;

public class TestAdapter extends ArrayAdapter<exam> {
    Context context;
    ArrayList<exam> arrayList;
    Long currentDate;
    DatabaseReference db;
    public TestAdapter(@NonNull Context context, ArrayList<exam> arrayList,Long currentDate) {
        super(context,0, arrayList);
        this.context=context;
        this.arrayList=arrayList;
        this.currentDate=currentDate;
        db= FirebaseDatabase.getInstance().getReference("results").child(getStream(STREAM)).child(SUBJECT);
    }

    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        final exam e=arrayList.get(position);
        final View finalConvertView = LayoutInflater.from(context).inflate(R.layout.test_layout,parent,false);

        ((TextView)finalConvertView.findViewById(R.id.subjecttestname)).setText(e.getExamname());
        ((TextView)finalConvertView.findViewById(R.id.subjecttestnumberquestion)).setText("Number of questions: "+e.getNumberquestion());
        ((TextView)finalConvertView.findViewById(R.id.subjecttesttime)).setText("Time: "+e.getTime()+" Minutes");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Long start=Long.parseLong(e.getDeadlinestart());
        Long end=Long.parseLong(e.getDeadlineend());
        ((TextView)finalConvertView.findViewById(R.id.subjecttestdeadline)).setText("Deadline: "+df.format(new Date(end)));
        if(currentDate<=end) {
            ((TextView) finalConvertView.findViewById(R.id.subjectteststatus)).setText("Status: Yet to start");
            (finalConvertView.findViewById(R.id.testCard)).setBackgroundResource(R.color.colorPrimary);
            if (currentDate >= start) {
                ((TextView) finalConvertView.findViewById(R.id.subjectteststatus)).setText("Status: LIVE");
                (finalConvertView.findViewById(R.id.testCard)).setBackgroundResource(R.color.live);
                if (ROLE == 0) {
                    db.child(e.getExamname()).child(USER).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount()==0){
                                finalConvertView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, GiveExam.class);
                                        ArrayList<question> questions = e.getArrayQuestion();
                                        Collections.shuffle(questions);
                                        intent.putParcelableArrayListExtra("questions",questions);
                                        intent.putExtra("examname", e.getExamname());
                                        intent.putExtra("time",e.getTime());
                                        intent.putExtra("numberquestions", e.getNumberquestion());
                                        context.startActivity(intent);
                                    }
                                });
                            }
                            else {
                                (finalConvertView.findViewById(R.id.testCard)).setBackgroundResource(R.color.attempted);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    finalConvertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getResult(e.getExamname());
                        }
                    });
                }
            }

        }
        else {
            ((TextView) finalConvertView.findViewById(R.id.subjectteststatus)).setText("Status: Expired");
            (finalConvertView.findViewById(R.id.testCard)).setBackgroundResource(R.color.attempted);
            if (ROLE==1){
                finalConvertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getResult(e.getExamname());
                    }
                });
            }
        }
        return finalConvertView;
    }

    void getResult(String examname){
        Intent intent = new Intent(context, ViewResults.class);
        intent.putExtra("examname", examname);
        context.startActivity(intent);
    }
}
