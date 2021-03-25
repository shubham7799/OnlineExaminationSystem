package com.example.onlineexaminationsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.onlineexaminationsystem.HomeActivity.getStream;
import static com.example.onlineexaminationsystem.Subject.STREAM;
import static com.example.onlineexaminationsystem.Subject.SUBJECT;

public class createexam extends AppCompatActivity{

    Button next;
    String deadlinestart,deadlineend;
    EditText numberquestion,mins,exname;
    TimePicker timePickerstart,timePickerend;
    CalendarView calendarViewstart,calendarViewend;
    DatabaseReference db;
    ProgressBar progressBar;
    int noquestion=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createexam);
        getSupportActionBar().setTitle("Create exam");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        next=findViewById(R.id.nextcreateexam);
        timePickerstart=findViewById(R.id.examstarttime);
        calendarViewstart=findViewById(R.id.createdeadlinestart);
        timePickerend=findViewById(R.id.examendtime);
        calendarViewend=findViewById(R.id.createdeadlineend);
        numberquestion=findViewById(R.id.createnoquestion);
        mins=findViewById(R.id.createminutes);
        exname=findViewById(R.id.createexamname);

        progressBar=findViewById(R.id.progresscreateexam);

        timePickerstart.setIs24HourView(true);
        timePickerend.setIs24HourView(true);

        db= FirebaseDatabase.getInstance().getReference("exams").child(getStream(STREAM)).child(SUBJECT);
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d=df.format(calendarViewstart.getDate());
        deadlinestart=d.substring(0,11);
        deadlineend=d.substring(0,11);
        calendarViewstart.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                deadlinestart=year+"-"+(month+1)+"-"+dayOfMonth+" ";
            }
        });
        calendarViewend.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                deadlineend=year+"-"+(month+1)+"-"+dayOfMonth+" ";
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                final String noq=numberquestion.getText().toString();
                final String min=mins.getText().toString();
                final String examname=exname.getText().toString();
                if (!(noq.isEmpty() || min.isEmpty() || examname.isEmpty())){
                    db.child(examname).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.hasChildren()){
                                next.setEnabled(false);
                                progressBar.setVisibility(View.VISIBLE);
                                noquestion=Integer.parseInt(noq);
                                if (noquestion>1){
                                    Intent i=new Intent(createexam.this,questionset.class);
                                    next.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    try {
                                        Long dstart=df.parse(deadlinestart+timePickerstart.getHour()+":"+timePickerstart.getMinute()+":00").getTime();
                                        Long dend=df.parse(deadlineend+timePickerend.getHour()+":"+timePickerend.getMinute()+":00").getTime();
                                        if (dstart<dend){
                                            i.putExtra("numberquestion",noquestion);
                                            i.putExtra("deadlinestart",String.valueOf(dstart));
                                            i.putExtra("deadlineend",String.valueOf(dend));
                                            i.putExtra("examname",examname);
                                            i.putExtra("time",Integer.parseInt(min));
                                            startActivity(i);
                                        }
                                        else {
                                            Toast.makeText(createexam.this,"Please select proper deadline!",Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.v("firebase","eror");
                                    }
                                }
                                else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    next.setEnabled(true);
                                    Toast.makeText(createexam.this,"No. of questions should be more than 1",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                next.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(createexam.this, "Exam already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(createexam.this,"Empty Fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}

