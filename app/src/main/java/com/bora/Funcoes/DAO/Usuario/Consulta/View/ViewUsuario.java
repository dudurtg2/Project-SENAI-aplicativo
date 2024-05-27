package com.bora.Funcoes.DAO.Usuario.Consulta.View;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bora.R;

public class ViewUsuario extends RecyclerView.ViewHolder {
    protected ImageView imageViewUsuario;

    protected TextView TextNome;
    protected TextView TextEndereco;
    protected TextView TextTelefone;
    public ViewUsuario(View v) {
        super(v);
        TextNome = v.findViewById(R.id.TextNome);
        TextEndereco = v.findViewById(R.id.TextEndereco);
        TextTelefone = v.findViewById(R.id.TextTelefone);
        imageViewUsuario = v.findViewById(R.id.imageViewUsuario);

    }

}
