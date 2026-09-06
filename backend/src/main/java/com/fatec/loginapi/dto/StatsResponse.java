package com.fatec.loginapi.dto;

public class StatsResponse {

    private String userId;
    private String username;
    private int level;
    private int vitorias;
    private int derrotas;

    public StatsResponse(String userId, String username, int level, int vitorias, int derrotas) {
        this.userId = userId;
        this.username = username;
        this.level = level;
        this.vitorias = vitorias;
        this.derrotas = derrotas;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getLevel() {
        return level;
    }

    public int getVitorias() {
        return vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }
}
