package com.bora.Functions.DTO.Dishes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishesDTO {
    private String description;
    private String name;
    private int imagem;
    private String uid;
    private String nome_cliente;
    private String nome_prato;
    private String uid_prato;
    private String uid_cliente;
    private  String data;
    public DishesDTO(String name, String description, String uid){
        this.name = name;
        this.description = description;
        this.uid = uid;
    }
    public DishesDTO(String name, String uid){
        this.name = name;
        this.uid = uid;
    }

    public DishesDTO(String nome_cliente, String nome_prato, String uid_cliente, String uid_prato, String data){
        this.nome_cliente = nome_cliente;
        this.nome_prato = nome_prato;
        this.uid_cliente = uid_cliente;
        this.uid_prato = uid_prato;
        this.data = data;
    }
}
