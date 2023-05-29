package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText searchET;
    Spinner searchBy;
    FirebaseServices services;
    List<Estate> estateList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<Estate> adapterList = new ArrayList<>();
    List<String> adapterIds, otherIds;
    ImageButton home, search, settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        String[] spinnerList = new String[]{"address", "city", "description", "floorNumber", "lotSize", "numOfRooms"};
        searchBy = findViewById(R.id.searchBySpinner);
        searchET = findViewById(R.id.listingSearchET);
        recyclerView = findViewById(R.id.searchRV);
        home = findViewById(R.id.SearchActivityHome);
        search = findViewById(R.id.SearchActivitySearch);
        search.setClickable(false);
        settings = findViewById(R.id.SearchActivitySettings);
        adapterIds = new ArrayList<>();
        otherIds = new ArrayList<>();
        services = new FirebaseServices();
        ArrayAdapter<String> arradapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerList);
        arradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchBy.setAdapter(arradapter);
        search(null);

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               search(editable.toString());

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainPage = new Intent(SearchActivity.this, MainPageActivity.class);
                startActivity(mainPage);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsPage = new Intent(SearchActivity.this, SettingsActivity.class);
                startActivity(settingsPage);
                finish();
            }
        });

    }
    private void search(String text){
        if (text == null) {
            services.getFire().collection("listings")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Estate estate = document.toObject(Estate.class);
                                    if (!estate.getOwnerId().equals(services.getAuth().getCurrentUser().getUid())){
                                        estateList.add(estate);
                                        otherIds.add(document.getId());
                                    }

                                }
                                adapterList.addAll(estateList);
                                adapterIds.addAll(otherIds);
                                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                                adapter = new RecyclerViewAdapter(SearchActivity.this, adapterList);
                                adapter.setClickListener(new RecyclerViewAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent showListingIntent = new Intent(SearchActivity.this, ShowListingActivity.class);
                                        showListingIntent.putExtra("listingId", adapterIds.get(position));
                                        showListingIntent.putExtra("listingImage", adapterList.get(position).getPicture());
                                        startActivity(showListingIntent);

                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            } else {
                                Log.d("Failed to get documents", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else{
            CollectionReference listings = services.getFire().collection("listings");
            Query searchQuery = listings.whereGreaterThanOrEqualTo(searchBy.getSelectedItem().toString(), text).whereLessThanOrEqualTo(searchBy.getSelectedItem().toString(), text + "\uF7FF");
            searchQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                adapterList.clear();
                                estateList.clear();
                                otherIds.clear();
                                adapterIds.clear();
                                if (adapter != null){
                                    adapter.notifyDataSetChanged();
                                }
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Estate estate = document.toObject(Estate.class);
                                    if (!estate.getOwnerId().equals(services.getAuth().getCurrentUser().getUid())){
                                        estateList.add(estate);
                                        otherIds.add(document.getId());
                                    }

                                }
                                adapterList.addAll(estateList);
                                adapterIds.addAll(otherIds);
                                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                                adapter = new RecyclerViewAdapter(SearchActivity.this, adapterList);
                                adapter.setClickListener(new RecyclerViewAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent showListingIntent = new Intent(SearchActivity.this, ShowListingActivity.class);
                                        showListingIntent.putExtra("listingId", adapterIds.get(position));
                                        showListingIntent.putExtra("listingImage", adapterList.get(position).getPicture());
                                        startActivity(showListingIntent);

                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            } else {
                                Log.d("Failed to get documents", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }

}