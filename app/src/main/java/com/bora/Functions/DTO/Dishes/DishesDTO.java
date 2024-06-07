package com.bora.Functions.DTO.Dishes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishesDTO {
    private String dishes;
    private String name;
    private int imagem;
    private String id;
    public DishesDTO(String name){
        this.name = name;
    }
}
