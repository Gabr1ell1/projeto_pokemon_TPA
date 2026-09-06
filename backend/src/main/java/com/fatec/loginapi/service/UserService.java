package com.fatec.loginapi.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fatec.loginapi.dto.AuthRequest;
import com.fatec.loginapi.dto.AuthResponse;
import com.fatec.loginapi.dto.RegistroRequest;
import com.fatec.loginapi.dto.StatsResponse;
import com.fatec.loginapi.dto.UpdateStatsRequest;
import com.fatec.loginapi.exception.ConflictException;
import com.fatec.loginapi.exception.NotFoundException;
import com.fatec.loginapi.exception.UnauthorizedException;
import com.fatec.loginapi.model.User;
import com.fatec.loginapi.repository.UserRepository;
import com.fatec.loginapi.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * POST /fatec/login/v1/user/save
     * Equivalente ao "save" do backend Node: valida username único,
     * criptografa a senha com bcrypt e persiste no MongoDB.
     */
    public void register(RegistroRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Usuário já cadastrado");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        List<String> roles = (request.getRoles() == null || request.getRoles().isEmpty())
                ? List.of("USER")
                : request.getRoles();

        User user = new User(request.getUsername(), hashedPassword, request.getEmail(), request.getCep(), roles);
        userRepository.save(user);
    }

    /**
     * POST /fatec/login/v1/auth
     * Compara a senha em texto puro com o hash salvo (bcrypt.compare).
     * O AuthController é quem coloca o token dentro do cookie HttpOnly,
     * por isso devolvemos o token separado do AuthResponse (que só tem
     * os dados que realmente podem ir no corpo/JSON).
     */
    public AuthResult authenticateAndGenerateToken(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Usuário ou senha inválidos");
        }

        String token = jwtService.generateToken(user.getId(), user.getRoles());

        return new AuthResult(token, user.getId(), user.getUsername());
    }

    // GET /fatec/login/v1/me - usado pelo app pra confirmar sessão ativa
    public AuthResponse getAuthResponse(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Usuário ou senha inválidos"));

        return new AuthResponse(user.getId(), user.getUsername());
    }

    public record AuthResult(String token, String userId, String username) {
    }

    public StatsResponse getStats(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return new StatsResponse(user.getId(), user.getUsername(), user.getLevel(), user.getVitorias(), user.getDerrotas());
    }

    public StatsResponse updateStats(String userId, UpdateStatsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (request.getLevel() != null) {
            user.setLevel(Integer.parseInt(request.getLevel()));
        }
        if (request.getVitorias() != null) {
            user.setVitorias(Integer.parseInt(request.getVitorias()));
        }
        if (request.getDerrotas() != null) {
            user.setDerrotas(Integer.parseInt(request.getDerrotas()));
        }

        userRepository.save(user);

        return new StatsResponse(user.getId(), user.getUsername(), user.getLevel(), user.getVitorias(), user.getDerrotas());
    }
}
