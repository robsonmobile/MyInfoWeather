package com.pcr.myinfoweather.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.pcr.myinfoweather.R;
import com.pcr.myinfoweather.models.user.UserAdress;

import java.util.List;

/**
 * Created by Paula on 24/05/2015.
 */
public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.ViewHolder> {

    private List<UserAdress> useraddresses;

    public SearchResultListAdapter(List<UserAdress> addressesList) {
        this.useraddresses = addressesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityItem;
        TextView stateItem;
        TextView countryItem;
        RatingBar favoriteItem;

        public ViewHolder(View itemView) {
            super(itemView);
            //declarar aqui imagens e textos que irão compor a custom view
            cityItem = (TextView) itemView.findViewById(R.id.cityItem);
            stateItem = (TextView) itemView.findViewById(R.id.stateItem);
            countryItem = (TextView) itemView.findViewById(R.id.countryItem);
            favoriteItem = (RatingBar) itemView.findViewById(R.id.favoriteItem);
        }
    }


    @Override
    public SearchResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cityItem.setText(useraddresses.get(position).getCity());
        holder.stateItem.setText(useraddresses.get(position).getState());
        holder.countryItem.setText(useraddresses.get(position).getCountry());

        //ratingbar here
    }

    @Override
    public int getItemCount() {
        return useraddresses.size();
    }
}
