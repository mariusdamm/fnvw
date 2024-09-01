package de.marius.fnvw.service;

import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dao.EntryRepository;
import de.marius.fnvw.dao.EntryTypeRepository;
import de.marius.fnvw.dto.EntryDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Entry;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    private final EntryTypeRepository entryTypeRepository;
    private final EntryGroupRepository entryGroupRepository;
    private final Logger logger = LoggerFactory.getLogger(EntryService.class);

    public EntryService(
            EntryRepository entryRepository,
            EntryTypeRepository entryTypeRepository,
            EntryGroupRepository entryGroupRepository
    ) {
        this.entryRepository = entryRepository;
        this.entryTypeRepository = entryTypeRepository;
        this.entryGroupRepository = entryGroupRepository;
    }

    public Entry addEntry(EntryDto body, AppUser user) throws DataNotFoundException, MissingDataException, ForbiddenDataException {
        if (user == null) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryService.addEntry", "AppUser user is null", "The AppUser passed by the calling function is null", "Throw DataNotFoundException", ""));
            throw new DataNotFoundException("Passed AppUser is null");
        }

        if (body == null) {
            logger.warn(LogInfo.toJson(LogLevel.ERROR, "EntryService.addEntry", "Entry body is null", "No entry was passed", "Throw MissingDataException", user.getUsername()));
            throw new MissingDataException("Entry body is null");
        }
        Entry entry = body.toEntry();

        List<EntryGroup> groups = entryGroupRepository.findByOwner(user);
        List<EntryType> allTypesOfUser = new ArrayList<>();
        for (EntryGroup group : groups) {
            allTypesOfUser.addAll(entryTypeRepository.findByGroup(group));
        }

        EntryType type = entryTypeRepository.findById(body.getEntryTypeId()).orElse(null);
        if (type == null) {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryService.addEntry", "Entrytype type is null", "EntryType with id " + body.getEntryTypeId() + " could not be found in the database", "Throw DataNotFoundException", user.getUsername()));
            throw new DataNotFoundException("EntryType with ID " + body.getEntryTypeId() + " could not be found in the database");
        }
        if (!allTypesOfUser.contains(type)) {
            logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryService.addEntry", "allTypesOfUser does not contain the type with id " + body.getEntryTypeId(), "EntryType does not belong to user", "Throw ForbiddenDataException", user.getUsername()));
            throw new ForbiddenDataException("EntryType with ID " + body.getEntryTypeId() + " does not belong to user " + user.getName());
        }
        entry.setType(type);

        entryRepository.save(entry);
        logger.info(LogInfo.toJson(LogLevel.INFO, "EntryService.addEntry", "", "", "Entry added successfully", user.getUsername()));
        return entry;
    }
}
