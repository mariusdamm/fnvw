package de.marius.fnvw.service;

import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dao.EntryTypeRepository;
import de.marius.fnvw.dto.EntryTypeDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.entity.EntryType;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryTypeService {

    private final EntryTypeRepository entryTypeRepository;
    private final EntryGroupRepository entryGroupRepository;
    private final Logger logger = LoggerFactory.getLogger(EntryTypeService.class);

    public EntryTypeService(EntryTypeRepository entryTypeRepository, EntryGroupRepository entryGroupRepository) {
        this.entryTypeRepository = entryTypeRepository;
        this.entryGroupRepository = entryGroupRepository;
    }

    public EntryType addEntryType(EntryTypeDto body, AppUser user) throws MissingDataException,
            DataNotFoundException, ForbiddenDataException {
        if (user == null) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryTypeService.addEntryType", "AppUser user is null", "AppUser user passed by calling function is null", "Throw MissingDataException", ""));
            throw new DataNotFoundException("User could not be found in the database");
        }

        if (body == null) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryTypeService.addEntryType", "EntryType body is null", "EntryType passed by calling function is null", "Throw MissingDataException", user.getUsername()));
            throw new MissingDataException("Entry body is null");
        }
        EntryType entryType = body.toEntryType();

        List<EntryGroup> allGroupsOfUser = entryGroupRepository.findByOwner(user);
        EntryGroup group = entryGroupRepository.findById(body.getEntryGroupId()).orElse(null);
        if (group == null) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryTypeService.addEntryType", "EntryGroup group is null", "EntryGroup with id " + body.getEntryGroupId() + " could not be found in the database", "Throw DataNotFoundException", user.getUsername()));
            throw new DataNotFoundException("EntryGroup with ID " + body.getEntryGroupId() + " could not be found in the database");
        }
        if (!allGroupsOfUser.contains(group)) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryTypeService.addEntryType", "allGroupsOfUser does not contain group with id " + group.getId(), "EntryGroup does not belong to user", "Throw ForbiddenDataException", user.getUsername()));
            throw new ForbiddenDataException("EntryType with ID " + body.getEntryGroupId() + " does not belong to user " + user.getName());
        }
        entryType.setGroup(group);

        entryTypeRepository.save(entryType);
        logger.info(LogInfo.toJson(LogLevel.INFO, "EntryTypeService.addEntryType", "", "", "Entry type added successfully", user.getUsername()));
        return entryType;
    }
}
