package com.bora.Funcoes.DAO.Usuario.Consulta.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bora.Funcoes.DTO.Usuario.UsuarioDTO;
import com.bora.R;

import java.util.List;

public class AdapterViewUsuario extends RecyclerView.Adapter<ViewUsuario>{

    Context context;
    List<UsuarioDTO> usuarioDTO;
    public AdapterViewUsuario(Context context, List<UsuarioDTO> usuarioDTO) {
        this.context = context;
        this.usuarioDTO = usuarioDTO;
    }

    @NonNull
    @Override
    public ViewUsuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewUsuario(LayoutInflater.from(context).inflate(R.layout.itensviews, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewUsuario holder, int position) {
        holder.TextNome.setText(usuarioDTO.get(position).getNome());
        holder.TextEndereco.setText(usuarioDTO.get(position).getEndereco());
        holder.TextTelefone.setText(usuarioDTO.get(position).getTelefone());
        holder.imageViewUsuario.setImageResource(R.drawable.a);
    }

    @Override
    public int getItemCount() {
        return usuarioDTO.size();
    }
}
