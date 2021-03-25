package com.example.onlineexaminationsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.onlineexaminationsystem.HomeActivity.ROLE;
import static com.example.onlineexaminationsystem.HomeActivity.getStream;

public class Subject extends AppCompatActivity {

    public static int STREAM=0;
    public static String SUBJECT=null;
    DatabaseReference db;
    ListView listView;
    TextView textView;
    ProgressBar progressBar;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        progressBar=findViewById(R.id.subjectprogress);
        textView=findViewById(R.id.subjectstatus);
        listView=findViewById(R.id.testlist);
        Intent i=getIntent();
        SUBJECT=i.getStringExtra("subject");
        STREAM=i.getIntExtra("stream",0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(SUBJECT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db= FirebaseDatabase.getInstance().getReference("exams").child(getStream(STREAM)).child(i.getStringExtra("subject"));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0){
                    textView.setVisibility(View.VISIBLE);
                }
                else {
                    ArrayList<exam> arrayList=new ArrayList<exam>();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        arrayList.add(snapshot.getValue(exam.class));
                    }
                    TestAdapter adapter=new TestAdapter(Subject.this,arrayList, Calendar.getInstance().getTimeInMillis());
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fab);
        if (ROLE==0){
            fab.setVisibility(View.GONE);
        }
        else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Subject.this,createexam.class);
                    startActivity(i);
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

}
