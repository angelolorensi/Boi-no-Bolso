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

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder>{

    private RecyclerViewInterface recyclerViewInterface;

    ArrayList<Listing> listings;

    public SearchResultsAdapter(ArrayList<Listing> listings, RecyclerViewInterface recyclerViewInterface){
        this.listings = listings;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setFilteredList(ArrayList<Listing> filteredList, RecyclerViewInterface recyclerViewInterface){
        this.listings = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultsAdapter.SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results_design, parent, false);
        SearchResultsAdapter.SearchResultsViewHolder searchResultsViewHolder = new SearchResultsAdapter.SearchResultsViewHolder(view, recyclerViewInterface);
        return searchResultsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.SearchResultsViewHolder holder, int position) {
        Listing listingsHelper = listings.get(position);

        Glide.with(holder.itemView.getContext()).load(listingsHelper.getImageUrl()).into(holder.image);
        holder.title.setText(listingsHelper.getTitle());
        holder.breed.setText(listingsHelper.getBreed());
        holder.maturity.setText(listingsHelper.getMaturity());
        holder.quantity.setText(listingsHelper.getQuantity());
        holder.price.setText(NumberFormat.getCurrencyInstance().format(listingsHelper.getPrice()));

    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class SearchResultsViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, breed, maturity, quantity, price;

        public SearchResultsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
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
