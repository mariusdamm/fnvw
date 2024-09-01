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
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entrygroup")
public class EntryGroupController {

    private final EntryGroupService entryGroupService;
    private final AppUserService userService;
    private final Logger logger = LoggerFactory.getLogger(EntryGroupController.class);

    public EntryGroupController(EntryGroupService entryGroupService, AppUserService userService) {
        this.entryGroupService = entryGroupService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<EntryGroupDto> createEntryGroup(@RequestBody EntryGroupDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug(LogInfo.toJson(LogLevel.DEBUG, "EntryGroupController.createEntryGroup", "", "", "Username retrieved", username));
        AppUser user = userService.getUserByUsername(username);
        try {
            EntryGroup entryGroup = entryGroupService.addEntryGroup(body, user);
            logger.info(LogInfo.toJson(LogLevel.INFO, "EntryGroupController.createEntryGroup", "", "", "EntryGroup created with id " + entryGroup.getId() + ". Return HttpStatus CREATED and data entryGroup as EntryGroupDto", username));
            return ResponseEntity.status(HttpStatus.CREATED).body(entryGroup.toDto());
        } catch (MissingDataException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryGroupController.createEntryGroup", e.getMessage(), "No EntryGroupDto was provided by the user", "Return HttpStatus BAD_REQUEST and data null", username));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (DataNotFoundException e) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryGroupController.createEntryGroup", e.getMessage(), "The username does not exist in the database", "Return HttpStatus NOT_FOUND and data null", username));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("")
    public ResponseEntity<EntryGroupDto> updateEntryGroup(@RequestBody EntryGroupDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug(LogInfo.toJson(LogLevel.DEBUG, "EntryGroupController.updateEntryGroup", "", "", "Username retrieved", username));
        AppUser user = userService.getUserByUsername(username);
        try {
            if (!userService.groupBelongsToUser(user, body.getId()))
                throw new ForbiddenDataException("EntryGroup " + body.getName() + " does not belong to user " + user.getUsername());
            EntryGroup entryGroup = entryGroupService.updateEntryGroup(body);
            logger.info(LogInfo.toJson(LogLevel.INFO, "EntryGroupController.updateEntryGroup", "", "", "EntryGroup updated with id " + entryGroup.getId() + ". Return HttpStatus OK and data entryGroup as EntryGroupDto", username));
            return ResponseEntity.status(HttpStatus.OK).body(entryGroup.toDto());
        } catch (ForbiddenDataException e) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryGroupController.updateEntryGroup", e.getMessage(), "The entryGroup does not belong to the user", "Return HttpStatus FORBIDDEN and data null", username));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (DataNotFoundException e) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryGroupController.updateEntryGroup", e.getMessage(), "The entryGroup does not exist in the database", "Return HttpStatus NOT_FOUND and data null", username));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{month}")
    public ResponseEntity<MonthDto> getEntryGroupsOfUserInMonth(@PathVariable int month) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug(LogInfo.toJson(LogLevel.DEBUG, "EntryGroupController.getEntryGroupsOfUserInMonth", "", "", "Username retrieved", username));
        AppUser user = userService.getUserByUsername(username);
        MonthDto monthDto = entryGroupService.getEntryGroupsOfUserInMonth(user, month);
        if (monthDto == null) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryGroupController.getEntryGroupsOfUserInMonth", "MonthDto is null", "No entry groups found for the user in the specified month", "Return HttpStatus NOT_FOUND and data null", username));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        logger.info(LogInfo.toJson(LogLevel.INFO, "EntryGroupController.getEntryGroupsOfUserInMonth", "", "", "Entry groups retrieved for the user in the specified month. Return HttpStatus OK and data monthDto", username));
        return ResponseEntity.status(HttpStatus.OK).body(monthDto);
    }
}
