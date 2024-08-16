package de.marius.fnvw.controller;

import de.marius.fnvw.dto.EntryTypeDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryType;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.service.AppUserService;
import de.marius.fnvw.service.EntryTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entrytype")
public class EntryTypeController {

    private final EntryTypeService entryTypeService;
    private final AppUserService appUserService;

    public EntryTypeController(EntryTypeService entryTypeService, AppUserService appUserService) {
        this.entryTypeService = entryTypeService;
        this.appUserService = appUserService;
    }

    @PostMapping("")
    public ResponseEntity<EntryTypeDto> createEntryType(@RequestBody EntryTypeDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = appUserService.getUserByUsername(username);
        try {
            EntryType type = entryTypeService.addEntryType(body, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(type.toDto());
        } catch (MissingDataException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ForbiddenDataException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
