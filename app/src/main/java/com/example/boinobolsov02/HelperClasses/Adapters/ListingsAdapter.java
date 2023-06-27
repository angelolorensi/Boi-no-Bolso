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
import com.example.boinobolsov02.HelperClasses.RecyclerViewInterface;
import com.example.boinobolsov02.R;

import java.util.ArrayList;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ListingsViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<ListingsHelper> listings;

    public ListingsAdapter(ArrayList<ListingsHelper> listings, RecyclerViewInterface recyclerViewInterface){
        this.listings = listings;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_listings_design, parent, false);
        ListingsViewHolder listingsViewHolder = new ListingsViewHolder(view, recyclerViewInterface);
        return listingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsViewHolder holder, int position) {
        ListingsHelper listingsHelper = listings.get(position);

        Glide.with(holder.itemView.getContext()).load(listingsHelper.getImageUrl()).into(holder.image);
        holder.title.setText(listingsHelper.getTitle());
        holder.breed.setText(listingsHelper.getBreed());
        holder.maturity.setText(listingsHelper.getMaturity());
        holder.quantity.setText(listingsHelper.getQuantity());
        holder.price.setText(listingsHelper.getPrice());

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class ListingsViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, breed, maturity, quantity, price;

        public ListingsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
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
