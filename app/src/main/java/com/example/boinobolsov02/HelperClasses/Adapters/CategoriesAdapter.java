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

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<Listing> listings;

    public CategoriesAdapter(ArrayList<Listing> listings, RecyclerViewInterface recyclerViewInterface){
        this.listings = listings;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_page_listings_design, parent, false);
        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view, recyclerViewInterface);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Listing categoriesHelper = listings.get(position);

        Glide.with(holder.itemView.getContext()).load(categoriesHelper.getImageUrl()).into(holder.image);
        holder.title.setText(categoriesHelper.getTitle());
        holder.breed.setText(categoriesHelper.getBreed());
        holder.maturity.setText(categoriesHelper.getMaturity());
        holder.quantity.setText(categoriesHelper.getQuantity());
        holder.price.setText(NumberFormat.getCurrencyInstance().format(categoriesHelper.getPrice()));

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, breed, maturity, quantity, price;

        public CategoriesViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
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
