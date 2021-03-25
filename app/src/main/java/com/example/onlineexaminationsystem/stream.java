package com.example.onlineexaminationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class stream extends AppCompatActivity {

    ListView listView;
    String[] subjectarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        listView = (ListView) findViewById(R.id.subjectlistview);

        Intent i=getIntent();
        final int stream=i.getIntExtra("stream",0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(stream==0){
            subjectarray= new String[]{"History", "Geography", "Psychology", "Sociology", "Marathi", "Political Science", "English", "Hindi"};
            getSupportActionBar().setTitle("Arts");
        }
        else if(stream==1){
            subjectarray= new String[]{"Accounting"};
            getSupportActionBar().setTitle("Commerce");
        }
        else if(stream==2){
            subjectarray= new String[]{"Physics", "Chemistry", "Biology","Electronics"};
            getSupportActionBar().setTitle("Science");
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,subjectarray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sub= parent.getItemAtPosition(position).toString();
                Intent i=new Intent(stream.this,Subject.class);
                i.putExtra("stream",stream);
                i.putExtra("subject",sub);

                startActivity(i);
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