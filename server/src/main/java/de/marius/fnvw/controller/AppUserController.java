package de.marius.fnvw.controller;

import de.marius.fnvw.dto.AppUserDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.service.AppUserService;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/self")
    public ResponseEntity<AppUserDto> getSelf() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserController.getSelf", "", "", "username " + username + " extracted from SecurityContext", username));

        AppUser user = appUserService.getUserByUsername(username);
        if (user == null) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "AppUserController.getSelf", "user is null", "User " + username + " not found in the database", "return HttpStatus NOT_FOUND and data null", username));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info(LogInfo.toJson(LogLevel.INFO, "AppUserController.getSelf", "User " + username + " retrieved", "", "return HttpStatus OK and data UserDto of the user", username));
        return ResponseEntity.status(HttpStatus.OK).body(user.toDto());
    }
}
