package com.example.boinobolsov02.HelperClasses.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.boinobolsov02.HelperClasses.ListingsHelper;
import com.example.boinobolsov02.R;

import java.util.ArrayList;

public class MylistingsAdapter extends RecyclerView.Adapter<MylistingsAdapter.MylistingsViewHolder> {

    ArrayList<ListingsHelper> listings;

    public MylistingsAdapter(ArrayList<ListingsHelper> listings){
        this.listings = listings;
    }

    @NonNull
    @Override
    public MylistingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylistings_page_listings_design, parent, false);
        MylistingsViewHolder mylistingsViewHolder = new MylistingsViewHolder(view);
        return mylistingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MylistingsViewHolder holder, int position) {
        ListingsHelper mylistingsHelper = listings.get(position);

        Glide.with(holder.itemView.getContext()).load(mylistingsHelper.getImageUrl()).into(holder.image);
        holder.title.setText(mylistingsHelper.getTitle());
        holder.breed.setText(mylistingsHelper.getBreed());
        holder.maturity.setText(mylistingsHelper.getMaturity());
        holder.quantity.setText(mylistingsHelper.getQuantity());
        holder.price.setText(mylistingsHelper.getPrice());

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class MylistingsViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, breed, maturity, quantity, price;

        public MylistingsViewHolder(@NonNull View itemView){
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.listing_image);
            title = itemView.findViewById(R.id.listing_title);
            breed = itemView.findViewById(R.id.listing_breed_txt);
            maturity = itemView.findViewById(R.id.listing_maturity_txt);
            quantity = itemView.findViewById(R.id.listing_quantity_txt);
            price = itemView.findViewById(R.id.listing_price_txt);
        }

    }


}
