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
    public DishesDTO(String name){
        this.name = name;
    }
    public DishesDTO(String name, String description, String uid){
        this.name = name;
        this.description = description;
        this.uid = uid;
    }
    public DishesDTO(String name, String uid){
        this.name = name;
        this.uid = uid;
    }
}
