package de.marius.fnvw.controller;

import de.marius.fnvw.dto.AppUserDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.service.AppUserService;
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
        logger.debug("Username {} retrieved - Method: getSelf - Action: Getting username from the security context", username);

        AppUser user = appUserService.getUserByUsername(username);
        if (user == null) {
            logger.error("User {} not found - Method: getSelf - Reason: The username does not exist in the database - Action: return HttpStatus Not_Found and data null", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        logger.info("User {} retrieved - Method: getSelf - Action: return HttpStatus Ok and data UserDto of user", username);
        return ResponseEntity.status(HttpStatus.OK).body(user.toDto());
    }
}
