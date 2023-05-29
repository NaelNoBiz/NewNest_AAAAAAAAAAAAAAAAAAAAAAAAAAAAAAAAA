package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    TextView goToSignUp ,goToReset;
    Button login;
    FirebaseServices services;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.loginEmailET);
        password = findViewById(R.id.loginPasswordET);
        login = findViewById(R.id.loginBTN);
        goToSignUp = findViewById(R.id.goToSignUpTV);
        goToReset=findViewById(R.id.gotoReset);
        services = new FirebaseServices();
        FirebaseUser user = services.getAuth().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(MainActivity.this, MainPageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d("", "onAuthStateChanged:signed_out");
        }
        goToReset.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
        Intent ResetPass= new Intent(MainActivity.this,ResetPasswordActivity.class);
        startActivity(ResetPass);
        }});
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(SignUpIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass =password.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                checkFields(pass, emailText);
                services.getAuth().signInWithEmailAndPassword(emailText, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent goToMainPage = new Intent(MainActivity.this, MainPageActivity.class);
                            startActivity(goToMainPage);
                        }
                        else{
                            if (task.getException() != null){
                                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

    }
    public Boolean checkFields(String pass, String emailText){
        if(emailText.length()  == 0|| pass.length() == 0) {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pass.length() < 6){
            Toast.makeText(this, "The password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}