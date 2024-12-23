package de.marius.fnvw.service;

import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dao.EntryRepository;
import de.marius.fnvw.dto.EntryDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Entry;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    private final AppUserService appUserService;
    private final EntryRepository entryRepository;
    private final EntryGroupRepository entryGroupRepository;
    private final Logger logger = LoggerFactory.getLogger(EntryService.class);

    public EntryService(
            EntryRepository entryRepository,
            EntryGroupRepository entryGroupRepository,
            AppUserService appUserService
    ) {
        this.entryRepository = entryRepository;
        this.entryGroupRepository = entryGroupRepository;
        this.appUserService = appUserService;
    }

    public Entry addEntry(EntryDto body, AppUser user) throws DataNotFoundException, MissingDataException, ForbiddenDataException {
        if (user == null) {
            if (logger.isErrorEnabled())
                logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryService.addEntry", "AppUser user is null", "The AppUser passed by the calling function is null", "Throw DataNotFoundException", ""));
            throw new DataNotFoundException("Passed AppUser is null");
        }

        if (body == null) {
            if (logger.isWarnEnabled())
                logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryService.addEntry", "Entry body is null", "No entry was passed", "Throw MissingDataException", user.getUsername()));
            throw new MissingDataException("Entry body is null");
        }
        Entry entry = body.toEntry();

        EntryGroup group = entryGroupRepository.findById(body.getEntryGroupId()).orElse(null);

        if (group == null) {
            if (logger.isErrorEnabled())
                logger.error(LogInfo.toJson(LogLevel.ERROR, "EntryService.addEntry", "Entrygroup type is null", "EntryGroup with id " + body.getEntryGroupId() + " could not be found in the database", "Throw DataNotFoundException", user.getUsername()));
            throw new DataNotFoundException("EntryGroup with ID " + body.getEntryGroupId() + " could not be found in the database");
        }
        if (!appUserService.groupBelongsToUser(user, body.getEntryGroupId())) {
            if (logger.isWarnEnabled())
                logger.warn(LogInfo.toJson(LogLevel.WARNING, "EntryService.addEntry", "appUserService.groupBelongsToUser returns false for EntryGroup with id " + body.getEntryGroupId(), "EntryGroup does not belong to user", "Throw ForbiddenDataException", user.getUsername()));
            throw new ForbiddenDataException("EntryGroup with ID " + body.getEntryGroupId() + " does not belong to user " + user.getName());
        }
        entry.setGroup(group);

        entryRepository.save(entry);
        if (logger.isInfoEnabled())
            logger.info(LogInfo.toJson(LogLevel.INFO, "EntryService.addEntry", "", "", "Entry added successfully", user.getUsername()));
        return entry;
    }
}
