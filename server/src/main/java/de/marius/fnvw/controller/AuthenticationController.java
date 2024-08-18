package de.marius.fnvw.controller;

import de.marius.fnvw.dto.LoginDto;
import de.marius.fnvw.dto.LoginResponseDto;
import de.marius.fnvw.dto.RegisterDto;
import de.marius.fnvw.exception.ConstraintException;
import de.marius.fnvw.service.AuthenticationService;
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
        try {
            LoginResponseDto dto = authenticationService.loginUser(body);
            logger.info("Login with username {} successful - Method: loginUser - Action: User successfully logged in and UserDto returned.", body.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (AuthenticationException e) {
            logger.warn("Login with username {} failed - Method: loginUser - Reason: The username or password is incorrect - Action: return HttpStatus Unauthorized and data null", body.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> registerUser(@RequestBody RegisterDto body) {
        body.setUsername(body.getUsername().replaceAll("\\s", ""));
        body.setPassword(body.getPassword().replaceAll("\\s", ""));
        body.setName(body.getName().replaceAll("\\s", ""));
        logger.info("Attempting to register user with username {} - Method: registerUser", body.getUsername());
        try {
            authenticationService.registerUser(body);
            LoginDto loginDto = new LoginDto(body.getUsername(), body.getPassword());
            LoginResponseDto dto = authenticationService.loginUser(loginDto);
            logger.info("Registration and login with username {} successful - Method: registerUser - Action: return HttpStatus Ok and data UserDto of user", body.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (AuthenticationException e) {
            logger.warn("{} - Registration failed for username {} - Method: registerUser - Reason: Authentication failed - Action: return HttpStatus Unauthorized and data null", e.getMessage(), body.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (IllegalArgumentException e) {
            logger.warn("{} - Registration failed for username {} - Method: registerUser - Reason: Name, Username and Password cannot be empty - Action: return HttpStatus Bad_Request and data null", e.getMessage(), body.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ConstraintException e) {
            logger.warn("{} - Registration failed for username {} - Method: registerUser - Reason: Constraint violation (username already exists) - Action: return HttpStatus Found and data null", e.getMessage(), body.getUsername());
            return ResponseEntity.status(HttpStatus.FOUND).body(null);
        }
    }
}
