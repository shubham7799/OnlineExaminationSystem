package com.example.onlineexaminationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.onlineexaminationsystem.HomeActivity.USER;
import static com.example.onlineexaminationsystem.HomeActivity.getStream;
import static com.example.onlineexaminationsystem.Subject.STREAM;
import static com.example.onlineexaminationsystem.Subject.SUBJECT;

public class ViewResults extends AppCompatActivity {

    String ename=null;
    TextView textView;
    ListView resultlistView;
    ProgressBar progressBar;
    ArrayList<String> datalist=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Result");
        Intent i=getIntent();
        textView=findViewById(R.id.resultstatus);
        resultlistView=findViewById(R.id.resultlist);
        progressBar=findViewById(R.id.resultprogress);
        ename=i.getStringExtra("examname");
        final ArrayList<result> arrayList=new ArrayList<result>();
        final ArrayList<String> name=new ArrayList<String>();
        final ArrayList<Integer> score=new ArrayList<Integer>();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("results").child(getStream(STREAM)).child(SUBJECT);
        final DatabaseReference sdb= FirebaseDatabase.getInstance().getReference("user");
        db.child(ename).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0){
                    textView.setVisibility(View.VISIBLE);
                }
                else {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        arrayList.add(snapshot.getValue(result.class));
                    }
                    for(int j=0;j<arrayList.size();j++){
                        score.add(arrayList.get(j).getScore());
                        sdb.child(arrayList.get(j).getUser()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String carid = dataSnapshot.getValue(String.class);
                                Log.v("firebase","ok"+carid);
                                name.add(carid);
                                if(name.size()==arrayList.size()){
                                    Log.v("firebase","score="+score.size());
                                    Log.v("firebase","name="+name.size());
                                    ResultAdapter adapter=new ResultAdapter(ViewResults.this,name,score);
                                    resultlistView.setAdapter(adapter);
                                    resultlistView.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

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
