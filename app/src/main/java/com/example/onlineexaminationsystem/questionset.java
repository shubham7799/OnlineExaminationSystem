package com.example.onlineexaminationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.onlineexaminationsystem.HomeActivity.USER;
import static com.example.onlineexaminationsystem.HomeActivity.getStream;
import static com.example.onlineexaminationsystem.Subject.STREAM;
import static com.example.onlineexaminationsystem.Subject.SUBJECT;

public class questionset extends AppCompatActivity {
    Button btnback,btnnext,btnfinish;
    TextView questionno;
    int pos=1;
    ArrayList<question> arrayList=new ArrayList<question>();
    EditText question,o1,o2,o3,o4;
    RadioGroup ans;
    int noofquestion,minutes;
    ProgressBar progressBar;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionset);
        getSupportActionBar().setTitle("Add questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        question=findViewById(R.id.editquestion);
        o1=findViewById(R.id.option1);
        o2=findViewById(R.id.option2);
        o3=findViewById(R.id.option3);
        o4=findViewById(R.id.option4);
        ans=findViewById(R.id.setansradio);
        btnback=findViewById(R.id.setquestionback);
        btnnext=findViewById(R.id.setquestionnext);
        btnfinish=findViewById(R.id.setquestionfinish);
        final Intent i=getIntent();
        noofquestion=i.getIntExtra("numberquestion",0);

        questionno=findViewById(R.id.settestquestion);
        questionno.setText("Question 1 of "+noofquestion);
        for (int j=0;j<noofquestion;j++){
            arrayList.add(new question());
        }
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getData()){
                    db= FirebaseDatabase.getInstance().getReference("exams").child(getStream(STREAM)).child(SUBJECT);
                    String ename=i.getStringExtra("examname");
                    db.child(ename).setValue(new exam(noofquestion,i.getStringExtra("deadlinestart"),i.getStringExtra("deadlineend"),i.getIntExtra("time",1),ename,arrayList));
                    startActivity((new Intent(questionset.this,HomeActivity.class)).putExtra("role",HomeActivity.ROLE).putExtra("user",USER));
            }
            else{
                Toast.makeText(questionset.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
            }}
        });
    }
    void next(){
        if(getData()){
            pos++;
            btnback.setEnabled(true);
            if (pos==noofquestion){
                btnnext.setEnabled(false);
            }
            display();
        }
        else {
            Toast.makeText(questionset.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
        }
    }
    void back(){
        if(getData()) {
            pos--;
            btnnext.setEnabled(true);
            if (pos == 1) {
                btnback.setEnabled(false);
            }
            display();
        }
        else{
            Toast.makeText(questionset.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
        }
    }
    void display(){
        question q=arrayList.get(pos-1);
        question.setText(q.getQuest());
        o1.setText(q.getO1());
        o2.setText(q.getO2());
        o3.setText(q.getO3());
        o4.setText(q.getO4());
        if (q.getAnswer()==0){
            ans.clearCheck();
        }
        else if (q.getAnswer()==1){
            ans.check(R.id.seto1);
        }
        else if (q.getAnswer()==2){
            ans.check(R.id.seto2);
        }
        else if (q.getAnswer()==3){
            ans.check(R.id.seto3);
        }
        else if (q.getAnswer()==4){
            ans.check(R.id.seto4);
        }
        questionno.setText("Question "+pos+" of "+noofquestion);
    }
    boolean getData(){
        question q=arrayList.get(pos-1);
        q.setQuest(question.getText().toString());
        String op1=o1.getText().toString();
        String op2=o2.getText().toString();
        String op3=o3.getText().toString();
        String op4=o4.getText().toString();
        if (op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty()){
            return false;
        }
        else {
            q.setO1(o1.getText().toString());
            q.setO2(o2.getText().toString());
            q.setO3(o3.getText().toString());
            q.setO4(o4.getText().toString());
        }
        int a=ans.getCheckedRadioButtonId();
        if (a==R.id.seto1){
            q.setAnswer(1);
        }
        else if (a==R.id.seto2){
            q.setAnswer(2);
        }
        else if (a==R.id.seto3){
            q.setAnswer(3);
        }
        else if(a==R.id.seto4){
            q.setAnswer(4);
        }
        else {
            return false;
        }
        arrayList.set(pos-1,q);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

}