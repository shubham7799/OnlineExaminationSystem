package com.example.onlineexaminationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class
HomeActivity extends AppCompatActivity {
    Button result;
    private CardView arts,science,commerce;
    public static int ROLE=0;
    public static String USER=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent i=getIntent();
        ROLE=i.getIntExtra("role",0);
        USER=i.getStringExtra("uid");
        arts=findViewById(R.id.artsbutton);
        science=findViewById(R.id.sciencebutton);
        commerce=findViewById(R.id.commercebutton);
        result=findViewById(R.id.homestudentresult);
        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStream(0);
            }
        });
        commerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStream(1);
            }
        });
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStream(2);
            }
        });
        if (ROLE==1){
            result.setVisibility(View.GONE);
        }
        else {
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this,StudentResult.class));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case  R.id.profileMenu:{
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                break;
            }
            case R.id.logout:{
                FirebaseAuth.getInstance().signOut();
                Intent inToMain=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(inToMain);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    void toStream(int stream){
        Intent i=new Intent(HomeActivity.this,stream.class);
        i.putExtra("stream",stream);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("No",null);
        builder.setMessage("Do you want to close the app?");
        builder.setTitle("Test Submitted");
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static String getStream(int stream){
        if (stream==0){
            return "arts";
        }
        else if (stream==1){
            return "commerce";
        }
        else if (stream==2){
            return "science";
        }
        return null;
    }
}
