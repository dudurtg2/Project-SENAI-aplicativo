package com.bora.Functions.DAO.Dishes.Queries.PrincipalView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bora.R;

public class ViewPrincipalDishes extends RecyclerView.ViewHolder {
    protected ImageView imageViewDishesPrincipal;
    protected TextView ButtonEditarPrincipal;

    public ViewPrincipalDishes(View v) {
        super(v);
        imageViewDishesPrincipal = (ImageView) v.findViewById(R.id.MainPratosPrincipalImage);
        ButtonEditarPrincipal = (TextView) v.findViewById(R.id.MainPratosPrincipalName);
    }
}
