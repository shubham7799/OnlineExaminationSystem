package com.example.onlineexaminationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    public EditText emailId, password,number,name;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAnalytics analytics;
    FirebaseUser user;
    private ImageView icon;
    private CardView card;
    String email,pwd,phone,uname;
    private  DatabaseReference mDatabase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignUp = findViewById(R.id.button);
        name=findViewById(R.id.signupname);
        tvSignIn = findViewById(R.id.textView);
        number = findViewById(R.id.editText4);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailId.getText().toString();
                pwd = password.getText().toString();
                phone=number.getText().toString();
                uname=name.getText().toString();
                mDatabase=FirebaseDatabase.getInstance().getReference("user");
                if(!(email.isEmpty() && pwd.isEmpty() && phone.isEmpty())){
                    if (phone.length()==10){
                        auth.signOut();
                        auth.createUserWithEmailAndPassword(email,pwd);
                        auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                user=FirebaseAuth.getInstance().getCurrentUser();
                                final String uid=user.getUid();
                                mDatabase.child(uid).setValue(new UserProfile(email,phone,uname,0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                auth.signOut();
                                                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                    else{
                        Toast.makeText(MainActivity.this, "Enter a valid phone number!", Toast.LENGTH_SHORT).show();
                    }
//

                }
                else if (email.isEmpty() || pwd.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        icon=findViewById(R.id.signupicon);
        card=findViewById(R.id.signupcard);
        if (user!=null){
            if (user.isEmailVerified()){
                final String uid=user.getUid();
                DatabaseReference df=FirebaseDatabase.getInstance().getReference("user");
                df.child(uid).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Intent i=new Intent(MainActivity.this,HomeActivity.class);
                        i.putExtra("uid",uid);
                        i.putExtra("role",dataSnapshot.getValue(Integer.class));
                        startActivity(i);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        icon.setVisibility(View.GONE);
                        card.setVisibility(View.VISIBLE);
                        getSupportActionBar().show();
                    }
                });
            }
            else{
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        }
        else {
            getSupportActionBar().show();
            icon.setVisibility(View.GONE);
            card.setVisibility(View.VISIBLE);
        }
    }
}
