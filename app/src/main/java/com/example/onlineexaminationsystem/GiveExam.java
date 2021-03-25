package com.example.onlineexaminationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.onlineexaminationsystem.HomeActivity.USER;
import static com.example.onlineexaminationsystem.HomeActivity.getStream;
import static com.example.onlineexaminationsystem.Subject.STREAM;
import static com.example.onlineexaminationsystem.Subject.SUBJECT;

public class GiveExam extends AppCompatActivity {

    int pos=1,noofquestion,min;
    Button nextbtn,backbtn,finishbtn;
    ArrayList<question> arrayList;
    TextView question,questionno,examtimer;
    ArrayList<Integer> answers;
    RadioButton o1,o2,o3,o4;
    RadioGroup ans;
    int correct=0,timemin,timesec;
    String ename=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_give_exam);

        Intent i=getIntent();
        noofquestion=i.getIntExtra("numberquestions",0);
        arrayList=i.getParcelableArrayListExtra("questions");
        ename=i.getStringExtra("examname");
        answers=new ArrayList<>();
        examtimer=findViewById(R.id.giveexamtimer);
        timemin=i.getIntExtra("time",5);
        timesec=59;
        examtimer.setText(timemin+":00");
        new CountDownTimer((long)i.getIntExtra("time",5)*60000, 1000) {
            int min=timemin-1,sec=59;
            public void onTick(long millisUntilFinished) {
                if (sec==0){
                    examtimer.setText("Time Remaining: "+min+":00");
                    min--;
                    sec=59;
                }
                else if(sec<10){
                    examtimer.setText("Time Remaining: "+min+":0"+sec);
                    sec--;
                }
                else {
                    examtimer.setText("Time Remaining: "+min+":"+sec);
                    sec--;
                }
            }
            public void onFinish() {
                examtimer.setText("Time Up!");
                finishTest();
            }

        }.start();
        for (int j=0;j<noofquestion;j++){
            answers.add(0);
        }
        questionno=findViewById(R.id.giveexamnumberquestion);
        ans=findViewById(R.id.giveexamansradio);
        nextbtn=findViewById(R.id.giveexamnext);
        backbtn=findViewById(R.id.gievexamback);
        finishbtn=findViewById(R.id.giveexamfinish);
        question=findViewById(R.id.giveexamquestion);
        o1=findViewById(R.id.examo1);
        o2=findViewById(R.id.examo2);
        o3=findViewById(R.id.examo3);
        o4=findViewById(R.id.examo4);
        display();

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTest();
            }
        });
    }
    void next(){
        getData();
        pos++;
        backbtn.setEnabled(true);
        if (pos==noofquestion){
            nextbtn.setEnabled(false);
        }
        display();
    }
    void back(){
        getData();
        pos--;
        nextbtn.setEnabled(true);
        if (pos==1){
            backbtn.setEnabled(false);
        }
        display();
    }
    void display(){
        int a=answers.get(pos-1);
        question q=arrayList.get(pos-1);
        if (a==0){
            ans.clearCheck();
        }
        else if(a==1){
            ans.check(R.id.examo1);
        }
        else if(a==2){
            ans.check(R.id.examo2);
        }
        else if(a==3){
            ans.check(R.id.examo3);
        }
        else if(a==4){
            ans.check(R.id.examo4);
        }
        o1.setText(q.getO1());
        o2.setText(q.getO2());
        o3.setText(q.getO3());
        o4.setText(q.getO4());
        question.setText(q.getQuest());
        questionno.setText("Question "+pos+" of "+noofquestion);
    }
    void getData(){
        int a=ans.getCheckedRadioButtonId();
        if (a==R.id.examo1){
            answers.set(pos-1,1);
        }
        else if (a==R.id.examo2){
            answers.set(pos-1,2);
        }
        else if (a==R.id.examo3){
            answers.set(pos-1,3);
        }
        else if(a==R.id.examo4){
            answers.set(pos-1,4);
        }
    }

    public void finishTest() {
        getData();
        for (int j=0;j<noofquestion;j++){
            if(arrayList.get(j).getAnswer()==answers.get(j)){
                correct++;
            }
        }
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("results").child(getStream(STREAM));
        db.child(SUBJECT).child(ename).child(USER).setValue(new result(USER,correct));
        DatabaseReference sdb=FirebaseDatabase.getInstance().getReference("user").child(USER).child("tests");
        sdb.child(sdb.push().getKey()).setValue(new StudentRecord(getStream(STREAM),SUBJECT,ename,correct));
        AlertDialog.Builder builder = new AlertDialog.Builder(GiveExam.this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity((new Intent(GiveExam.this,HomeActivity.class)).putExtra("role",HomeActivity.ROLE).putExtra("user",USER));
            }
        });
        builder.setMessage("Your Score is "+correct+" out of "+noofquestion)
                .setCancelable(false);
        builder.setTitle("Test Submitted");
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            AlertDialog.Builder builder = new AlertDialog.Builder(GiveExam.this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishTest();
                }
            });
            builder.setNegativeButton("No",null);
            builder.setMessage("Do you want finish the test?");
            builder.setTitle("Finish Test");
            AlertDialog alert = builder.create();
            alert.show();
        }
        return true;
    }
}
