package toubani.badreddine.carloacation.services;

import toubani.badreddine.carloacation.dto.auth.AuthResponse;
import toubani.badreddine.carloacation.dto.auth.LoginRequest;
import toubani.badreddine.carloacation.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
