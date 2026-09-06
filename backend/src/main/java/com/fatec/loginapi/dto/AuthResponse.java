package com.fatec.loginapi.dto;

/**
 * Autenticação agora é via cookie HttpOnly (ver AuthController.auth()),
 * então o token NÃO vai mais no corpo da resposta - só userId/username,
 * que o app usa pra exibir dados e saber quem está logado.
 */
public class AuthResponse {

    private String userId;
    private String username;

    public AuthResponse(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
