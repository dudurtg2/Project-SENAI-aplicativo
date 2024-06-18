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
    private String data;
    private String price;
    private String status;
    private String table;

    public DishesDTO(String name, String description, String uid, String table){
        this.name = name;
        this.description = description;
        this.uid = uid;
        this.table = table;
    }
    public DishesDTO(String name, String uid, String price){
        this.name = name;
        this.uid = uid;
        this.price = price;
    }

    public DishesDTO(String UID, String nome_cliente, String nome_prato, String uid_cliente, String uid_prato, String data, String price, String status){
        this.uid = UID;
        this.nome_cliente = nome_cliente;
        this.nome_prato = nome_prato;
        this.uid_cliente = uid_cliente;
        this.uid_prato = uid_prato;
        this.data = data;
        this.price = price;
        this.status = status;
    }
}
