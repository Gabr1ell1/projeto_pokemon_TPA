package com.fatec.loginapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.loginapi.dto.AuthRequest;
import com.fatec.loginapi.dto.AuthResponse;
import com.fatec.loginapi.dto.RegistroRequest;
import com.fatec.loginapi.exception.UnauthorizedException;
import com.fatec.loginapi.service.UserService;

import jakarta.validation.Valid;

/**
 * Base path igual ao que o app monta em pokemonApi.ts:
 * `${EXPO_PUBLIC_LOCAL_API_URL}/fatec/login/v1`
 *
 * Autenticação via cookie HttpOnly: o JWT continua sendo gerado do
 * mesmo jeito (JwtService), mas em vez de ir no corpo da resposta,
 * viaja dentro de um cookie "auth_token". O app não precisa mais
 * guardar/reenviar token manualmente - o cookie faz isso sozinho.
 */
@RestController
@RequestMapping("/fatec/login/v1")
public class AuthController {

    private static final String COOKIE_NAME = "auth_token";

    private final UserService userService;

    // true em produção (Render, HTTPS) - false em dev local (HTTP).
    // Ver application.properties: app.cookie.secure
    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Chamado por register.tsx -> services/pokemonApi.ts -> register()
    @PostMapping("/user/save")
    public ResponseEntity<Void> save(@Valid @RequestBody RegistroRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Chamado por index.tsx (login) -> AuthContext.signIn() -> pokemonApi.login()
    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@Valid @RequestBody AuthRequest request) {
        UserService.AuthResult result = userService.authenticateAndGenerateToken(request);

        ResponseCookie cookie = buildCookie(result.token(), expirationMs / 1000);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponse(result.userId(), result.username()));
    }

    // Limpa o cookie no dispositivo. Chamado por AuthContext.signOut().
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = buildCookie("", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    // O app chama isso pra saber se o cookie ainda é válido e quem está
    // logado, em vez de decodificar um JWT localmente (não dá mais,
    // já que o token nunca chega no corpo da resposta).
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException("Não autenticado");
        }

        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getAuthResponse(userId));
    }

    private ResponseCookie buildCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure)
                // SameSite=None exige Secure=true (HTTPS). Em dev local
                // (HTTP) usamos Lax, que funciona sem HTTPS.
                .sameSite(cookieSecure ? "None" : "Lax")
                .path("/")
                .maxAge(maxAgeSeconds)
                .build();
    }
}
