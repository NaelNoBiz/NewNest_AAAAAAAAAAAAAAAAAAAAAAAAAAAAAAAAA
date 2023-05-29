package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password, username;
    TextView goToLogin;
    Button signup;
    FirebaseServices services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.signUpEmailET);
        username = findViewById(R.id.signUpUsernameET);
        password = findViewById(R.id.signUpPasswordET);
        signup = findViewById(R.id.signUpBTN);
        goToLogin = findViewById(R.id.goToLoginTV);
        services = new FirebaseServices();

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpIntent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(SignUpIntent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass =password.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String userText = username.getText().toString().trim();
                if (checkFields(pass, emailText, userText)){
                    services.getAuth().createUserWithEmailAndPassword(emailText, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignUpActivity.this, "Successfully created new user", Toast.LENGTH_SHORT).show();
                            if (task.isSuccessful()){
                                if (task.getResult() != null && task.getResult().getUser() != null){
                                    String userId = task.getResult().getUser().getUid();
                                    User user = new User(userText, userId, new ArrayList<>(), new ArrayList<>());
                                    services.getFire().collection("users").document(user.getUserId()).set(user);
                                    Intent goToMainPage = new Intent(SignUpActivity.this, MainPageActivity.class);
                                    startActivity(goToMainPage);
                                    finish();
                                }
                            }
                        }
                    });
                }


            }
        });
    }
    public Boolean checkFields(String pass, String emailText, String userText){
        if(emailText.length()  == 0|| pass.length() == 0 || userText.length() == 0) {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pass.length() < 6){
            Toast.makeText(this, "The password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(userText.length() < 4){
            Toast.makeText(this, "Your username must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}