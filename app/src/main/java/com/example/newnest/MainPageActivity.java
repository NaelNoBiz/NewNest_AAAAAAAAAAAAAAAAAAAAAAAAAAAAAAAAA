package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    FirebaseServices services;
    Button addListing, myListings, reloadListings, savedListings;
    // This list is for estates not by user
    List<Estate> estateList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    Boolean isMyListing = false;
    // This list is for estates owned by user
    List<Estate> mylistings = new ArrayList<>();
    // This list is for the saved listings of the user
    List<Estate> savedlistings = new ArrayList<>();
    // this is the adapter list
    List<Estate> adapterList = new ArrayList<>();
    List<String> adapterIds, myIds, otherIds, savedListingIds;
    ImageButton home, search, settings;
    boolean isSavedListing = false;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        addListing = findViewById(R.id.addListingBTN);
        myListings = findViewById(R.id.myListingsBTN);
        reloadListings = findViewById(R.id.reloadListingsBTN);
        savedListings = findViewById(R.id.showSavedListings);
        home = findViewById(R.id.Home);
        search = findViewById(R.id.Search);
        settings = findViewById(R.id.Settings);
        services = new FirebaseServices();
        recyclerView = findViewById(R.id.listingsRV);

        adapterIds = new ArrayList<>();
        myIds = new ArrayList<>();
        otherIds = new ArrayList<>();
        savedListingIds = new ArrayList<>();

        if (services.getAuth().getCurrentUser() != null){
            userId = services.getAuth().getCurrentUser().getUid();
        }
        addListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddListing = new Intent(MainPageActivity.this, AddListingActivity.class);
                startActivity(goToAddListing);
            }
        });
        reloadListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        myListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMyListing){
                    isMyListing = true;
                    myListings.setText("Other Listings");
                    adapterList.clear();
                    adapterIds.clear();
                    adapterList.addAll(mylistings);
                    adapterIds.addAll(myIds);
                }
                else{
                    isMyListing = false;
                    myListings.setText("My Listings");
                    adapterList.clear();
                    adapterIds.clear();
                    adapterList.addAll(estateList);
                    adapterIds.addAll(otherIds);
                }
                adapter.notifyDataSetChanged();

            }
        });
        services.getFire().collection("listings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Estate estate = document.toObject(Estate.class);
                                if (estate.getOwnerId().equals(services.getAuth().getCurrentUser().getUid())){
                                    mylistings.add(estate);
                                    myIds.add(document.getId());
                                }
                                else {
                                    estateList.add(estate);
                                    otherIds.add(document.getId());
                                }

                            }
                            adapterList.addAll(estateList);
                            adapterIds.addAll(otherIds);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainPageActivity.this));
                            adapter = new RecyclerViewAdapter(MainPageActivity.this, adapterList);
                            adapter.setClickListener(new RecyclerViewAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent showListingIntent = new Intent(MainPageActivity.this, ShowListingActivity.class);
                                    showListingIntent.putExtra("listingId", adapterIds.get(position));
                                    showListingIntent.putExtra("listingImage", adapterList.get(position).getPicture());
                                    startActivity(showListingIntent);

                                }
                            });
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("Error occurred", "Error getting documents: ", task.getException());
                        }
                    }
        });
        loadSavedListings();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSearch = new Intent(MainPageActivity.this, SearchActivity.class);
                startActivity(goToSearch);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSettings = new Intent(MainPageActivity.this, SettingsActivity.class);
                startActivity(goToSettings);
            }
        });

        savedListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSavedListing){
                    isSavedListing = true;
                    savedListings.setText("All Listings");
                    adapterList.clear();
                    adapterIds.clear();
                    adapterList.addAll(savedlistings);
                    adapterIds.addAll(savedListingIds);
                }
                else{
                    isSavedListing = false;
                    savedListings.setText("Saved");
                    adapterList.clear();
                    adapterIds.clear();
                    adapterList.addAll(estateList);
                    adapterIds.addAll(otherIds);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }


    private void loadSavedListings(){
        services.getFire().collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        User user = task.getResult().toObject(User.class);
                        for (int i = 0; i < user.getSaved().size(); i++){
                            services.getFire().collection("listings").document(user.getSaved().get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful() ){
                                        if (task.getResult() != null){
                                            Estate estate = task.getResult().toObject(Estate.class);
                                            savedlistings.add(estate);
                                            savedListingIds.add(task.getResult().getId());

                                        }

                                    }
                                    else{
                                        Log.d("Error occurred", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        }
                    }

                }
                else{
                    Log.d("Error occurred", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}