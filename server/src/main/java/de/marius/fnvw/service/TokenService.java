package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final AppUserRepository appUserRepository;
    private final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, AppUserRepository appUserRepository) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.appUserRepository = appUserRepository;
    }

    public String generateJwt(Authentication auth) {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "TokenService.generateJwt", "", "", "Generating JWT", auth.getName()));
        Instant now = Instant.now();
        Instant expired = now.plus(30, ChronoUnit.MINUTES);

        String scope = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(now).subject(auth.getName())
                .claim("roles", scope).expiresAt(expired).build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        if (logger.isInfoEnabled())
            logger.info(LogInfo.toJson(LogLevel.INFO, "TokenService.generateJwt", "", "", "JWT generated successfully", auth.getName()));
        return token;
    }

    private String getUsernameFromToken(String token) {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "TokenService.getUsernameFromToken", "", "", "Extracting username from token", ""));
        try {
            token = token.substring(token.indexOf(" ") + 1);
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getSubject();
            if (logger.isInfoEnabled())
                logger.info(LogInfo.toJson(LogLevel.INFO, "TokenService.getUsernameFromToken", "", "", "return username", username));
            return username;
        } catch (JwtException e) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "TokenService.getUsernameFromToken", "JwtException", e.getMessage(), "return empty string", ""));
            return "";
        }
    }

    public AppUser getUserFromToken(String token) throws DataNotFoundException {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "TokenService.getUserFromToken", "", "", "Getting user from token", ""));
        String username = getUsernameFromToken(token);
        AppUser user = appUserRepository.findByUsername(username).orElseThrow(() -> {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "TokenService.getUserFromToken", "User not found in the database", "There is no user with username " + username + " (gathered by TokenService.getUsernameFromToken)", "Throw DataNotFoundException", username));
            return new DataNotFoundException("There is no user with username " + username + " in the database");
        });
        if (logger.isInfoEnabled())
            logger.info(LogInfo.toJson(LogLevel.INFO, "TokenService.getUserFromToken", "", "", "User retrieved from token", username));
        return user;
    }

    public String getRolesFromToken(String token) {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "TokenService.getRolesFromToken", "", "", "Getting roles from token", ""));
        try {
            token = token.substring(token.indexOf(" ") + 1);
            Jwt jwt = jwtDecoder.decode(token);
            if (logger.isInfoEnabled())
                logger.info(LogInfo.toJson(LogLevel.INFO, "TokenService.getRolesFromToken", "", "", "Roles retrieved from token", ""));
            return jwt.getClaim("roles");
        } catch (JwtException e) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "TokenService.getRolesFromToken", "JwtException", e.getMessage(), "Exception caught while decoding token", ""));
            return null;
        }
    }
}
