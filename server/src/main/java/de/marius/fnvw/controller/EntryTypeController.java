package de.marius.fnvw.controller;

import de.marius.fnvw.dto.EntryTypeDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryType;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.service.AppUserService;
import de.marius.fnvw.service.EntryTypeService;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entrytype")
public class EntryTypeController {

    private final EntryTypeService entryTypeService;
    private final AppUserService appUserService;
    private final Logger logger = LoggerFactory.getLogger(EntryTypeController.class);

    public EntryTypeController(EntryTypeService entryTypeService, AppUserService appUserService) {
        this.entryTypeService = entryTypeService;
        this.appUserService = appUserService;
    }

    @PostMapping("")
    public ResponseEntity<EntryTypeDto> createEntryType(@RequestBody EntryTypeDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug(LogInfo.toJson(LogLevel.DEBUG, "EntryTypeController.createEntryType", "", "", "Username retrieved", username));
        AppUser user = appUserService.getUserByUsername(username);
        try {
            EntryType type = entryTypeService.addEntryType(body, user);
            logger.info(LogInfo.toJson(LogLevel.INFO, "EntryTypeController.createEntryType", "", "", "EntryType created with id " + type.getId() + ". Return HttpStatus CREATED and data entryType as EntryTypeDto", username));
            return ResponseEntity.status(HttpStatus.CREATED).body(type.toDto());
        } catch (MissingDataException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryTypeController.createEntryType", e.getMessage(), "No EntryTypeDto was provided by the user", "Return HttpStatus BAD_REQUEST and data null", username));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (DataNotFoundException e) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryTypeController.createEntryType", e.getMessage(), "The username/entrygroup does not exist in the database", "Return HttpStatus NOT_FOUND and data null", username));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ForbiddenDataException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryTypeController.createEntryType", e.getMessage(), "The entryType does not belong to the user", "Return HttpStatus FORBIDDEN and data null", username));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
