package com.example.boinobolsov02.HelperClasses.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.boinobolsov02.HelperClasses.Models.Listing;
import com.example.boinobolsov02.HelperClasses.RecyclerViewInterface;
import com.example.boinobolsov02.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MylistingsAdapter extends RecyclerView.Adapter<MylistingsAdapter.MylistingsViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<Listing> listings;

    public MylistingsAdapter(ArrayList<Listing> listings, RecyclerViewInterface recyclerViewInterface){
        this.listings = listings;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MylistingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylistings_page_listings_design, parent, false);
        MylistingsViewHolder mylistingsViewHolder = new MylistingsViewHolder(view, recyclerViewInterface);
        return mylistingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MylistingsViewHolder holder, int position) {
        Listing mylistingsHelper = listings.get(position);

        Glide.with(holder.itemView.getContext()).load(mylistingsHelper.getImageUrl()).into(holder.image);
        holder.title.setText(mylistingsHelper.getTitle());
        holder.breed.setText(mylistingsHelper.getBreed());
        holder.maturity.setText(mylistingsHelper.getMaturity());
        holder.quantity.setText(mylistingsHelper.getQuantity());
        holder.price.setText(NumberFormat.getCurrencyInstance().format(mylistingsHelper.getPrice()));

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class MylistingsViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, breed, maturity, quantity, price;

        public MylistingsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.listing_image);
            title = itemView.findViewById(R.id.listing_title);
            breed = itemView.findViewById(R.id.listing_breed_txt);
            maturity = itemView.findViewById(R.id.listing_maturity_txt);
            quantity = itemView.findViewById(R.id.listing_quantity_txt);
            price = itemView.findViewById(R.id.listing_price_txt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getBindingAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }

    }


}
