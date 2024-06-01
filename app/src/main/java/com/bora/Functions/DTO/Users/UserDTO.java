package com.bora.Functions.DTO.Users;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class UserDTO {
    private String name;
    private String address;
    private String number;
    private String birthDate;
    private String cpf;
    private String rg;
    private int imagem;
    private String id;


    public UserDTO(String name, String address, String number, String id) {
        this.name = name;
        this.address = address;
        this.number = number;
        this.id = id;
    }

    public UserDTO(String name, String address, String number, String birthDate, String cpf, String rg) {
        this.name = name;
        this.address = address;
        this.number = number;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.rg = rg;
    }
}
