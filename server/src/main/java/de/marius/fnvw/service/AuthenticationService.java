package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.dto.LoginDto;
import de.marius.fnvw.dto.LoginResponseDto;
import de.marius.fnvw.dto.RegisterDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Role;
import de.marius.fnvw.exception.ConstraintException;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(AuthenticationManager authenticationManager, TokenService tokenService, AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto loginUser(LoginDto body) {
        logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AuthenticationService.loginUser", "", "", "Attempting to authenticate user", body.getUsername()));
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));

        logger.info(LogInfo.toJson(LogLevel.INFO, "AuthenticationService.loginUser", "", "", "User authenticated successfully. Token generated", body.getUsername()));
        String token = tokenService.generateJwt(auth);
        return new LoginResponseDto(token);
    }

    public void registerUser(RegisterDto body) throws ConstraintException {
        logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AuthenticationService.registerUser", "", "", "Attempting to register user", body.getUsername()));
        if (body.getUsername().isBlank() || body.getPassword().isBlank() || body.getName().isBlank()) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "AuthenticationService.registerUser", "IllegalArgumentException", "Username, password or name is blank", "Throw IllegalArgumentException", body.getUsername()));
            throw new IllegalArgumentException("Username, password and name must not be empty");
        }

        if (appUserRepository.findByUsername(body.getUsername()).isPresent()) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "AuthenticationService.registerUser", "Username already present in the Database", "Username already taken", "Throw ConstraintException", body.getUsername()));
            throw new ConstraintException("Username " + body.getUsername() + " is already taken");
        }

        Role userRole = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));

        List<Role> roles = List.of(userRole);
        appUserRepository.save(new AppUser(
                body.getName(), body.getUsername(), passwordEncoder.encode(body.getPassword()), roles
        ));
        logger.info(LogInfo.toJson(LogLevel.INFO, "AuthenticationService.registerUser", "", "", "User registered successfully", body.getUsername()));
    }
}
