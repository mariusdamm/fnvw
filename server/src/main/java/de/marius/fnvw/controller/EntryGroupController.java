package de.marius.fnvw.controller;

import de.marius.fnvw.dto.EntryGroupDto;
import de.marius.fnvw.dto.MonthDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.service.AppUserService;
import de.marius.fnvw.service.EntryGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entrygroup")
public class EntryGroupController {

    private final EntryGroupService entryGroupService;
    private final AppUserService userService;

    public EntryGroupController(EntryGroupService entryGroupService, AppUserService userService) {
        this.entryGroupService = entryGroupService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<EntryGroupDto> createEntryGroup(@RequestBody EntryGroupDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userService.getUserByUsername(username);
        try {
            EntryGroup entryGroup = entryGroupService.addEntryGroup(body, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(entryGroup.toDto());
        } catch (MissingDataException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("")
    public ResponseEntity<EntryGroupDto> updateEntryGroup(@RequestBody EntryGroupDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userService.getUserByUsername(username);
        try {
            if (!userService.groupBelongsToUser(user, body.getId()))
                throw new ForbiddenDataException("EntryGroup " + body.getName() + " does not belong to user " + user.getUsername());
            EntryGroup entryGroup = entryGroupService.updateEntryGroup(body);
            return ResponseEntity.status(HttpStatus.OK).body(entryGroup.toDto());
        } catch (ForbiddenDataException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{month}")
    public ResponseEntity<MonthDto> getEntryGroupsOfUserInMonth(@PathVariable int month) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userService.getUserByUsername(username);
        MonthDto monthDto = entryGroupService.getEntryGroupsOfUserInMonth(user, month);
        if(monthDto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(monthDto);
    }
}
