package com.bora.Funcoes.DTO.Usuario;

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
    private String id;


    public UsuarioDTO(String nome, String endereco, String telefone,String id) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.id = id;
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
