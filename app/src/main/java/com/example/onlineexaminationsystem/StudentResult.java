package com.example.onlineexaminationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.onlineexaminationsystem.HomeActivity.USER;
import static com.example.onlineexaminationsystem.HomeActivity.getStream;

public class StudentResult extends AppCompatActivity {

    ArrayList<StudentRecord> arrayList;
    ListView listView;
    ProgressBar progressBar;
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Previous Results");
        listView=findViewById(R.id.resultlist);
        progressBar=findViewById(R.id.resultprogress);
        status=findViewById(R.id.resultstatus);
        arrayList=new ArrayList<StudentRecord>();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("user").child(USER);
            db.child("tests").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount()!=0){
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                            arrayList.add(snapshot.getValue(StudentRecord.class));
                        }
                        ResultAdapter resultAdapter=new ResultAdapter(StudentResult.this,arrayList);
                        listView.setAdapter(resultAdapter);
                        listView.setVisibility(View.VISIBLE);
                    }
                    else {
                        status.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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
