package com.fatec.loginapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.loginapi.dto.StatsResponse;
import com.fatec.loginapi.dto.UpdateStatsRequest;
import com.fatec.loginapi.service.UserService;

/**
 * Usado pela tela de perfil/dashboard (getProfile/updateProfile em
 * services/pokemonApi.ts). Protegido por JWT (ver SecurityConfig).
 */
@RestController
@RequestMapping("/fatec/login/v1/stats")
public class StatsController {

    private final UserService userService;

    public StatsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public StatsResponse getStats(@PathVariable String userId) {
        return userService.getStats(userId);
    }

    @PutMapping("/{userId}")
    public StatsResponse updateStats(@PathVariable String userId, @RequestBody UpdateStatsRequest request) {
        return userService.updateStats(userId, request);
    }
}
