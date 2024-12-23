package de.marius.fnvw.controller;

import de.marius.fnvw.dto.EntryDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Entry;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.service.AppUserService;
import de.marius.fnvw.service.EntryService;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entry")
public class EntryController {

    private final EntryService entryService;
    private final AppUserService appUserService;
    private final Logger logger = LoggerFactory.getLogger(EntryController.class);

    public EntryController(EntryService entryService, AppUserService appUserService) {
        this.entryService = entryService;
        this.appUserService = appUserService;
    }

    @PostMapping("")
    public ResponseEntity<EntryDto> addEntry(@RequestBody EntryDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "EntryController.addEntry", "", "", "Username retrieved", username));
        AppUser user = appUserService.getUserByUsername(username);
        try {
            Entry entry = entryService.addEntry(body, user);
            if (logger.isInfoEnabled())
                logger.info(LogInfo.toJson(LogLevel.INFO, "EntryController.addEntry", "", "", "Entry created with id " + entry.getId() + ". Return HttpStatus CREATED and data entry as EntryDto", username));
            return ResponseEntity.status(HttpStatus.CREATED).body(entry.toDto());
        } catch (DataNotFoundException e) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryController.addEntry", e.getMessage(), "The username / entrytype does not exist in the database", "Return HttpStatus NOT_FOUND and data null", username));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (MissingDataException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryController.addEntry", e.getMessage(), "No EntryDto was provided by the user", "Return HttpStatus BAD_REQUEST and data null", username));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ForbiddenDataException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryController.addEntry", e.getMessage(), "The entrytype does not belong to the user", "Return HttpStatus FORBIDDEN and data null", username));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
