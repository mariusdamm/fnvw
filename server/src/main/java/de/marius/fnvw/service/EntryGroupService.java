package de.marius.fnvw.service;

import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dto.EntryGroupDto;
import de.marius.fnvw.dto.MonthDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryGroupService {

    private final EntryGroupRepository entryGroupRepository;
    private final Logger logger = LoggerFactory.getLogger(EntryGroupService.class);

    public EntryGroupService(EntryGroupRepository entryGroupRepository) {
        this.entryGroupRepository = entryGroupRepository;
    }

    public EntryGroup addEntryGroup(EntryGroupDto body, AppUser user) throws MissingDataException, DataNotFoundException {
        if (user == null) {
            if (logger.isErrorEnabled())
                logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryGroupService.addEntryGroup", "AppUser user is null", "null was passed by calling function", "Throw DataNotFoundException", ""));
            throw new DataNotFoundException("By calling function passed user is null");
        }

        if (body == null) {
            if (logger.isWarnEnabled())
                logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryGroupService.addEntryGroup", "MissingDataException", "Entry body is null", "Throw MissingDataException", user.getUsername()));
            throw new MissingDataException("Entry body is null");
        }
        EntryGroup entryGroup = body.toEntryGroup();
        entryGroup.setOwner(user);

        entryGroupRepository.save(entryGroup);
        if (logger.isInfoEnabled())
            logger.info(LogInfo.toJson(LogLevel.INFO, "EntryGroupService.addEntryGroup", "", "", "Entry group added successfully", user.getUsername()));
        return entryGroup;
    }

    public MonthDto getEntryGroupsOfUserInMonth(AppUser user, int month) {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "EntryGroupService.getEntryGroupsOfUserInMonth", "", "", "Getting entry groups for user in month", user.getUsername()));
        MonthDto dto = new MonthDto(String.valueOf(month));
        List<EntryGroup> groups = entryGroupRepository.findByOwner(user);

        for (EntryGroup group : groups) {
            if (group.getMonth() != month) {
                continue;
            }

            if (group.getIsIntake())
                dto.addIntakeGroup(group.toDto());
            else
                dto.addSpendingGroup(group.toDto());
        }

        if (dto.getIntakeGroups().isEmpty() && dto.getSpendingGroups().isEmpty()) {
            if (logger.isWarnEnabled())
                logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryGroupService.getEntryGroupsOfUserInMonth", "intakeGroups and spendingGroups are both empty lists", "No entry groups found for the user in the specified month", "Return null", user.getUsername()));
            return null;
        }

        if (logger.isInfoEnabled())
            logger.info(LogInfo.toJson(LogLevel.INFO, "EntryGroupService.getEntryGroupsOfUserInMonth", "", "", "Entry groups retrieved successfully", user.getUsername()));
        return dto;
    }

    public EntryGroup updateEntryGroup(EntryGroupDto body) throws DataNotFoundException {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "EntryGroupService.updateEntryGroup", "", "", "Updating entry group with id " + body.getId(), ""));
        EntryGroup group = entryGroupRepository.findById(body.getId()).orElse(null);
        if (group == null) {
            if (logger.isErrorEnabled())
                logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryGroupService.updateEntryGroup", "EntryGroup group is null", "EntryGroup with id " + body.getId() + " could not be found in the database", "Throw DataNotFoundException", ""));
            throw new DataNotFoundException("EntryGroup with id " + body.getId() + " could not be found");
        }
        group.setName(body.getName());
        group.setIsIntake(body.getIsIntake());
        if (logger.isInfoEnabled())
            logger.info(LogInfo.toJson(LogLevel.INFO, "EntryGroupService.updateEntryGroup", "", "", "EntryGroup with id " + group.getId() + " updated successfully", ""));
        return entryGroupRepository.save(group);
    }
}
