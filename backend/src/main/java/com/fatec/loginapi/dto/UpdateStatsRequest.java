package com.fatec.loginapi.dto;

/**
 * Espelha UpdateStatsRequest do app: os campos chegam como string
 * ({ level, vitorias, derrotas }: string), então convertemos ao usar.
 */
public class UpdateStatsRequest {

    private String level;
    private String vitorias;
    private String derrotas;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getVitorias() {
        return vitorias;
    }

    public void setVitorias(String vitorias) {
        this.vitorias = vitorias;
    }

    public String getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(String derrotas) {
        this.derrotas = derrotas;
    }
}
