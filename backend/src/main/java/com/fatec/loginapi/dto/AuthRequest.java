package com.fatec.loginapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Espelha o tipo AuthRequest do app: { username, password }
 */
public class AuthRequest {

    @NotBlank(message = "username é obrigatório")
    private String username;

    @NotBlank(message = "password é obrigatório")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
