package com.example.newnest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Estate> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private FirebaseServices services;
    final long FIVE_MEGABYTE = 1024 * 1024 * 5;
    // data is passed into the constructor
    RecyclerViewAdapter(Context context, List<Estate> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listing_recyclerview_layout, parent, false);
        services = new FirebaseServices();
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Estate estate = mData.get(position);
        services.getStorage().getReference(estate.getPicture()).getBytes(FIVE_MEGABYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                    holder.picture.setImageBitmap(bitmap);
                    holder.picture.setRotation(90);
                }
                else{
                    Log.d("Download Image:", task.getException().toString());
                }
            }
        });

        holder.address.setText(estate.getAddress());
        holder.rooms.setText("Rooms: \n" + estate.getNumOfRooms());
        holder.floors.setText("Floors: \n" + estate.getFloorNumber());
        holder.size.setText("Size: \n" + estate.getSize() + "m²");
        holder.price.setText("Price: \n" + estate.getPrice() + " ₪");
        if (Objects.equals(estate.getListingType(), "Rent")){
            holder.price.setText(holder.price.getText() + "/Month");
        }



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView address;
        ImageView picture;
        TextView rooms;
        TextView floors;
        TextView size;
        TextView price;

        ViewHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.rowAddressName);
            picture = itemView.findViewById(R.id.rowPicture);
            rooms = itemView.findViewById(R.id.rowRoomNumber);
            floors = itemView.findViewById(R.id.rowFloorNumber);
            size = itemView.findViewById(R.id.rowSize);
            price = itemView.findViewById(R.id.rowPrice);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Estate getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
