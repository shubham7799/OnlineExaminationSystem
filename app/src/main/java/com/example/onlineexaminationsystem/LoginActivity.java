package com.example.onlineexaminationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btnlogin;
    TextView tvSignUp, exLogin;
    FirebaseAuth auth;
    DatabaseReference db;
    String emailtxt,passwordtxt;
    int role;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotPassword=findViewById(R.id.forgotpassword);
        tvSignUp=findViewById(R.id.signup);
        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        btnlogin = findViewById(R.id.loginbutton);
        auth=FirebaseAuth.getInstance();
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailtxt=email.getText().toString();
                passwordtxt=password.getText().toString();
                if(!(emailtxt.isEmpty() && passwordtxt.isEmpty())){
                    auth.signInWithEmailAndPassword(emailtxt,passwordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser u=auth.getCurrentUser();
                            if (u.isEmailVerified() || emailtxt.equals("jaihindcollege@gmail.com")){
                                final String uid=u.getUid();
                                FirebaseDatabase.getInstance().getReference("user").child(uid).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                                        i.putExtra("uid",uid);
                                        i.putExtra("role",dataSnapshot.getValue(Integer.class));
                                        startActivity(i);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Email Verification required!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if (emailtxt.isEmpty() || passwordtxt.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        auth.signOut();
        startActivity(new Intent(this,MainActivity.class));
    }
}