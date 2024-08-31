package de.marius.fnvw.controller;

import de.marius.fnvw.dto.LoginDto;
import de.marius.fnvw.dto.LoginResponseDto;
import de.marius.fnvw.dto.RegisterDto;
import de.marius.fnvw.exception.ConstraintException;
import de.marius.fnvw.service.AuthenticationService;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginDto body) {
        body.setUsername(body.getUsername().replaceAll("\\s", ""));
        body.setPassword(body.getPassword().replaceAll("\\s", ""));
        logger.info(LogInfo.toJson(LogLevel.INFO, "AuthenticationController.loginUser", "", "", "Attempting to login user", body.getUsername()));
        try {
            LoginResponseDto dto = authenticationService.loginUser(body);
            logger.info(LogInfo.toJson(LogLevel.INFO, "AuthenticationController.loginUser", "", "", "User successfully logged in and HttpStatus OK and data LoginResponseDto with JWT returned", body.getUsername()));
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (AuthenticationException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "AuthenticationController.loginUser", "Login with username " + body.getUsername() + " failed", "The username or password is incorrect", "Return HttpStatus UNAUTHORIZED and data null", body.getUsername()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> registerUser(@RequestBody RegisterDto body) {
        body.setUsername(body.getUsername().replaceAll("\\s", ""));
        body.setPassword(body.getPassword().replaceAll("\\s", ""));
        logger.info(LogInfo.toJson(LogLevel.INFO, "AuthenticationController.registerUser", "", "", "Attempting to register user", body.getUsername()));
        try {
            authenticationService.registerUser(body);
            logger.info(LogInfo.toJson(LogLevel.INFO, "AuthenticationController.registerUser", "", "", "successfully created user", body.getUsername()));
            LoginDto loginDto = new LoginDto(body.getUsername(), body.getPassword());
            LoginResponseDto dto = authenticationService.loginUser(loginDto);
            logger.info(LogInfo.toJson(LogLevel.INFO, "AuthenticationController.registerUser", "", "", "successfully logged in user. Return HttpStatus OK and data LoginResponseDto with JWT", body.getUsername()));
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (AuthenticationException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "AuthenticationController.registerUser", "Could not login user", "AuthenticationException: Username or password is incorrect, although the user was just created with them", "Return HttpStatus UNAUTHORIZED and data null", body.getUsername()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (IllegalArgumentException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "AuthenticationController.registerUser", "Registration failed", "IllegalArgumentException: Name, username and password cannot be blank", "Return HttpStatus BAD_REQUEST and data null", body.getUsername()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ConstraintException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "AuthenticationController.registerUser", "Registration failed", "ConstraintException: Username already exists", "Return HttpStatus FOUND and data null", body.getUsername()));
            return ResponseEntity.status(HttpStatus.FOUND).body(null);
        }
    }
}
