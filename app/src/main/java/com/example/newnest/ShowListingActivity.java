package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

public class ShowListingActivity extends AppCompatActivity {

    TextView address, city,price, lotSize,size,roomNumber,floorNumber,phone,ownername;
    FirebaseServices services;
    ImageView listingPicture;
    Button deleteListing;
    ImageButton saveListing;
    Boolean isSaved = false;
    String userId;
    final long FIVE_MEGABYTE = 1024 * 1024 * 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_listing);
        address = findViewById(R.id.showListingAddressTV);
        city = findViewById(R.id.showListingCityTV);
        price = findViewById(R.id.showListingPriceTV);
        lotSize = findViewById(R.id.showListingLotSizeTV);
        size = findViewById(R.id.showListingSizeTV);
        roomNumber = findViewById(R.id.showListingRoomNumberTV);
        floorNumber = findViewById(R.id.showListingFloorNumberTV);
        phone = findViewById(R.id.showListingPhoneNumberTV);
        listingPicture = findViewById(R.id.showListingPictureIV);
        deleteListing = findViewById(R.id.deleteListingBTN);
        saveListing = findViewById(R.id.saveListingBTN);
        ownername=findViewById(R.id.showListingOwnerName);
        String listingId = getIntent().getStringExtra("listingId");
        String imagePath = getIntent().getStringExtra("listingImage");

        services = new FirebaseServices();
        userId = services.getAuth().getCurrentUser().getUid();
        services.getStorage().getReference(imagePath).getBytes(FIVE_MEGABYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                    listingPicture.setImageBitmap(bitmap);
                    listingPicture.setRotation(90);
                }
                else{
                    Log.d("Download Image:", task.getException().toString());
                }
            }
        });
        services.getFire().collection("listings").document(listingId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Estate estate = task.getResult().toObject(Estate.class);
                    address.setText(estate.getAddress());
                    city.setText("City: " + estate.getCity());
                    price.setText("Price: " + estate.getPrice());
                    lotSize.setText("Lot Size: " + estate.getLotSize() + " m²");
                    size.setText("Size: " + estate.getSize() + " m²");
                    roomNumber.setText("Room Number: " + estate.getNumOfRooms());
                    floorNumber.setText("Floor Number: " + estate.getFloorNumber());
                    services.getFire().collection("users").document(estate.getOwnerId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.isSuccessful()){
                          User user = task.getResult().toObject(User.class);
                           ownername.setText("Owner: "+user.getUsername());
                       }
                        }
                    });

                    phone.setText(estate.getNumber());
                    if (userId.equals(estate.getOwnerId())){
                        deleteListing.setVisibility(View.VISIBLE);
                        deleteListing.setClickable(true);
                    }
                    else{
                        saveListing.setVisibility(View.VISIBLE);
                        saveListing.setClickable(true);
                    }
                }
            }
        });
        services.getFire().collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    if (user != null){
                        ArrayList<String> saved = user.getSaved();
                        if(saved.contains(listingId)){
                            isSaved = true;
                            saveListing.setImageDrawable(getDrawable(R.drawable.ic_baseline_bookmark_border_24));
                        }
                    }

                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone.getText().toString()));
                startActivity(intent);
            }
        });
        deleteListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                services.getFire().collection("listings").document(listingId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ShowListingActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                DocumentReference userInformationReference = services.getFire().collection("users").document(userId);
                userInformationReference.update("listedEstates", FieldValue.arrayRemove(listingId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            finish();
                        }
                    }
                });
            }
        });
        saveListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSaved){
                    DocumentReference userInformationReference = services.getFire().collection("users").document(userId);
                    userInformationReference.update("saved", FieldValue.arrayRemove(listingId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ShowListingActivity.this, "Successfully removed Listing", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(ShowListingActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    DocumentReference userInformationReference = services.getFire().collection("users").document(userId);
                    userInformationReference.update("saved", FieldValue.arrayUnion(listingId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ShowListingActivity.this, "Successfully added Listing", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(ShowListingActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}