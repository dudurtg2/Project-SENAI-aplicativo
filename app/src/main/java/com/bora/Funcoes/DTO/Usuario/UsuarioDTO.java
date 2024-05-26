package com.bora.Funcoes.DTO.Usuario;


import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class UsuarioDTO {
    private String nome;
    private String endereco;
    private String telefone;
    private String dataNascimento;
    private String cpf;
    private String rg;
    private int imagem;

    public UsuarioDTO(String nome, String endereco, String telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public UsuarioDTO(String nome, String endereco, String telefone, String dataNascimento, String cpf, String rg) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.rg = rg;
    }
}
