package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResetPasswordActivity extends AppCompatActivity {
 TextView Goback;
 Button Send;
 EditText Email;
 FirebaseServices services;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Goback=findViewById(R.id.BackToPrevPage);
        Send=findViewById(R.id.ResetBTN);
        Email=findViewById(R.id.resetPASSET);
        services=new FirebaseServices();
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                services.getAuth().sendPasswordResetEmail(Email.toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Reset link sent", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else Toast.makeText(ResetPasswordActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }


                });


            }
        });

}}