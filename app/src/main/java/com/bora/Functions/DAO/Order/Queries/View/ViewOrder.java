package com.bora.Functions.DAO.Order.Queries.View;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bora.R;

public class ViewOrder extends RecyclerView.ViewHolder {
    protected ImageView DishesImageOrderShow;
    protected ImageView imageViewEditar;
    protected TextView DishesClientNameOrderShow;
    protected TextView DishesNameOrderShow;

    public ViewOrder(View v) {
        super(v);
        DishesClientNameOrderShow = v.findViewById(R.id.DishesClientNameOrderShow);
        DishesNameOrderShow = v.findViewById(R.id.DishesNameOrderShow);
        DishesImageOrderShow = v.findViewById(R.id.DishesImageOrderShow);
        imageViewEditar = v.findViewById(R.id.imageView2);

    }

}
