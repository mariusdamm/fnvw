package de.marius.fnvw.controller;

import de.marius.fnvw.dto.EntryDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Entry;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.service.AppUserService;
import de.marius.fnvw.service.EntryService;
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
        logger.debug("Username {} retrieved - Method: addEntry - Action: Getting username from the security context", username);
        AppUser user = appUserService.getUserByUsername(username);
        try {
            Entry entry = entryService.addEntry(body, user);
            logger.info("Entry with id {} created - Method: addEntry - Action: return HttpStatus Created and data EntryDto of entry", entry.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(entry.toDto());
        } catch (DataNotFoundException e) {
            logger.error("{} - User not found with username {} - Method: addEntry - Reason: The username does not exist in the database - Action: return HttpStatus Not_Found and data null", e.getMessage(), username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (MissingDataException e) {
            logger.warn("{} - The EntryDto is null - Method: addEntry - Action: return HttpStatus Bad_Request and data null", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ForbiddenDataException e) {
            logger.warn("{} - The Entry does not belong to the user - Method: addEntry - Action: return HttpStatus Forbidden and data null", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
