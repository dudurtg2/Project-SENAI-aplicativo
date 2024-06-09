package com.bora.Functions.DAO.Dishes.Queries.View;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bora.R;

public class ViewDishes extends RecyclerView.ViewHolder{
    protected ImageView imageViewDishes;
    protected ImageView imageDishesBack;
    protected TextView ButtonEditar;;

    public ViewDishes(View v) {
        super(v);
        imageViewDishes = (ImageView) v.findViewById(R.id.MainPratosimage);
        imageDishesBack = (ImageView) v.findViewById(R.id.MainPratosimageBack);
        ButtonEditar = (TextView) v.findViewById(R.id.MainPratosButton);
    }
}
