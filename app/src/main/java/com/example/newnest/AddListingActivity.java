package com.example.newnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AddListingActivity extends AppCompatActivity {

    Button create, cancel;
    Spinner estateType, listingType;
    EditText address, city, floorNumber, roomNumber, price, lotSize, size, description, phoneNumber;
    FirebaseServices services;
    ImageView listingPicture;
    public static final int PICK_IMAGE = 1;
    Bitmap listingImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        create = findViewById(R.id.confirmCreateListingBTN);
        cancel = findViewById(R.id.cancelCreateListingBTN);
        estateType = findViewById(R.id.estateTypeSpinner);
        listingType = findViewById(R.id.listingTypeSpinner);
        description = findViewById(R.id.listingDescriptionET);
        address = findViewById(R.id.listingAddressET);
        city = findViewById(R.id.listingCityET);
        floorNumber = findViewById(R.id.listingFloorNumberET);
        roomNumber = findViewById(R.id.numberOfRoomsET);
        price = findViewById(R.id.listingPriceET);
        lotSize = findViewById(R.id.listingLotSizeET);
        size = findViewById(R.id.listingSizeET);
        listingPicture = findViewById(R.id.listingProfilePicture);
        phoneNumber = findViewById(R.id.listingPhoneET);
        services = new FirebaseServices();
        initSpinner(estateType, new String[] {"select","House","Apartment", "Storage"});
        initSpinner(listingType, new String[] {"select","Rent", "Sell"});

        size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lotSize.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (services.getAuth().getCurrentUser()!= null){
                  if(address!=null&&city!=null&&floorNumber!=null&& roomNumber!=null&& price!=null&& lotSize!=null&& size!=null&& phoneNumber!=null)
                    createListing();
                  else{
                      Toast.makeText(AddListingActivity.this, "Please fill the empty spaces", Toast.LENGTH_SHORT).show();

                  }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listingPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddListingActivity.this.getContentResolver(), data.getData());
                        listingPicture.setImageBitmap(bitmap);
                        listingImage = bitmap;
                        listingPicture.setRotation(90);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(AddListingActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createListing() {
        String addressValue = address.getText().toString();
        String cityValue = city.getText().toString();
        int floorNum = Integer.parseInt(floorNumber.getText().toString());
        int roomNum = Integer.parseInt(roomNumber.getText().toString());
        int lotSizeValue = Integer.parseInt(lotSize.getText().toString());
        int sizeValue = Integer.parseInt(size.getText().toString());
        int priceValue = Integer.parseInt(price.getText().toString());
        String descriptionValue = description.getText().toString();
        String estateTypeValue = estateType.getSelectedItem().toString();
        String listingTypeValue = listingType.getSelectedItem().toString();
        String userId = services.getAuth().getCurrentUser().getUid();
        String number = phoneNumber.getText().toString();
        // Uploading the image to firebase storage
        String imagePath = UploadImageToFirebase();
        // Creating the estate object
        if( !Objects.equals(estateType.getSelectedItem().toString(), "select")&&!Objects.equals(listingType.getSelectedItem().toString(), "select")) {
          if(imagePath!=null){

            Estate estate = new Estate(estateTypeValue, addressValue, cityValue, roomNum, floorNum, imagePath
                    , priceValue, userId, listingTypeValue, descriptionValue, sizeValue, lotSizeValue, number);
            services.getFire().collection("listings").add(estate).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        String documentId = task.getResult().getId();
                        DocumentReference userInformationReference = services.getFire().collection("users").document(userId);
                        userInformationReference.update("listedEstates", FieldValue.arrayUnion(documentId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddListingActivity.this, "Successfully added Listing", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddListingActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(AddListingActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
          else Toast.makeText(this,"please select an image",Toast.LENGTH_SHORT).show();

       }
        else Toast.makeText(this,"please select Listing and estate type",Toast.LENGTH_SHORT).show();

    }

    private void initSpinner(Spinner spinner, String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private String UploadImageToFirebase(){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        listingImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference ref = services.getStorage().getReference("listingPictures/" + UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            }
        });
        return ref.getPath();
    }
}