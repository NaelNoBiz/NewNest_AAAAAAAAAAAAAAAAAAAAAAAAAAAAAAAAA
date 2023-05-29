package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class SettingsActivity extends AppCompatActivity {

    Button updateUsername, signout;
    EditText username;
    FirebaseServices services;
    String userId;
    ImageButton home, search, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        services = new FirebaseServices();
        updateUsername = findViewById(R.id.updateUserBTN);
        signout = findViewById(R.id.signOutBTN);
        username = findViewById(R.id.userNameET);
        home = findViewById(R.id.SettingsActivityHome);
        search = findViewById(R.id.SettingsActivitySearch);
        settings = findViewById(R.id.SettingsActivitySettings);
        settings.setClickable(false);
        if(services.getAuth().getCurrentUser() != null){
            userId = services.getAuth().getCurrentUser().getUid();
        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                services.getAuth().signOut();
                Intent login = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        });
        services.getFire().collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    username.setText(user.getUsername());
                }
            }
        });
        updateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length() > 3){
                    services.getFire().collection("users").document(userId).update("username", username.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SettingsActivity.this, "Successfully updated name", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SettingsActivity.this, "Name must be at least 4 letters", Toast.LENGTH_SHORT).show();
                }
               
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainPage = new Intent(SettingsActivity.this, MainPageActivity.class);
                startActivity(mainPage);
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchPage = new Intent(SettingsActivity.this, SearchActivity.class);
                startActivity(searchPage);
                finish();
            }
        });

    }
}