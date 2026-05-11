package toubani.badreddine.carloacation.services.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.auth.AuthResponse;
import toubani.badreddine.carloacation.dto.auth.LoginRequest;
import toubani.badreddine.carloacation.dto.auth.RegisterRequest;
import toubani.badreddine.carloacation.entities.AppUser;
import toubani.badreddine.carloacation.enums.Role;
import toubani.badreddine.carloacation.exceptions.BusinessException;
import toubani.badreddine.carloacation.repositories.UserRepository;
import toubani.badreddine.carloacation.security.AppUserDetailsService;
import toubani.badreddine.carloacation.security.JwtService;
import toubani.badreddine.carloacation.services.AuthService;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new BusinessException("Email and password are required.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("An account already exists with this email.");
        }

        AppUser user = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of(Role.ROLE_CLIENT))
                .build();
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return buildResponse(user, jwtService.generateToken(userDetails));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new BusinessException("Invalid email or password.");
        }
        AppUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password."));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return buildResponse(user, jwtService.generateToken(userDetails));
    }

    private AuthResponse buildResponse(AppUser user, String token) {
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInSeconds(jwtService.getExpirationMs() / 1000)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles().stream().map(Role::name).collect(Collectors.toSet()))
                .build();
    }
}
