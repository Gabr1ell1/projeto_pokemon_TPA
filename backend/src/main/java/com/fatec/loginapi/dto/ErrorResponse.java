package com.fatec.loginapi.dto;

/**
 * O app lê error.response.data.message (ver register.tsx / AuthContext.tsx),
 * então todo erro da API precisa vir nesse formato: { "message": "..." }
 */
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
