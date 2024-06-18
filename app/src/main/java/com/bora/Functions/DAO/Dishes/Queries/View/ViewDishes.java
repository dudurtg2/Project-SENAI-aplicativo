package com.bora.Functions.DAO.Dishes.Queries.View;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bora.R;

public class ViewDishes extends RecyclerView.ViewHolder{
    protected ImageView imageViewDishes;
    protected TextView MainPratosName;
    protected TextView MainPriceName;

    public ViewDishes(View v) {
        super(v);
        imageViewDishes = (ImageView) v.findViewById(R.id.MainPratosimage);
        MainPratosName = (TextView) v.findViewById(R.id.MainPratosName);
        MainPriceName = (TextView) v.findViewById(R.id.MainPriceName);
    }
}
