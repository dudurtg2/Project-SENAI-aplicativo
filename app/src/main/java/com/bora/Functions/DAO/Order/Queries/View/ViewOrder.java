package com.bora.Functions.DAO.Order.Queries.View;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bora.R;

public class ViewOrder extends RecyclerView.ViewHolder {
    protected ImageView DishesImageOrderShow;
    protected TextView DishesPriceOrderShow;
    protected TextView DishesStatusOrderShow;
    protected TextView DishesClientNameOrderShow;
    protected TextView DishesNameOrderShow;
    protected Button CancelOrder;
    protected Button ConfirmOrder;

    public ViewOrder(View v) {
        super(v);
        DishesClientNameOrderShow = v.findViewById(R.id.DishesClientNameOrderShow);
        DishesNameOrderShow = v.findViewById(R.id.DishesNameOrderShow);
        DishesImageOrderShow = v.findViewById(R.id.DishesImageOrderShow);
        DishesPriceOrderShow = v.findViewById(R.id.DishesPriceOrderShow);
        DishesStatusOrderShow = v.findViewById(R.id.DishesStatusOrderShow);
        CancelOrder = v.findViewById(R.id.DishesConfirmOrderShow);
        ConfirmOrder = v.findViewById(R.id.DishesDeleteOrderShow);

    }

}
