package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dao.EntryRepository;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Entry;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.util.LogInfo;
import de.marius.fnvw.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final EntryGroupRepository entryGroupRepository;
    private final EntryRepository entryRepository;
    private final Logger logger = LoggerFactory.getLogger(AppUserService.class);

    public AppUserService(AppUserRepository appUserRepository, EntryGroupRepository entryGroupRepository,
                          EntryRepository entryRepository) {
        this.appUserRepository = appUserRepository;
        this.entryGroupRepository = entryGroupRepository;
        this.entryRepository = entryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserService.loadUserByUsername", "", "", "Loading user by username", username));
        return appUserRepository.findByUsername(username).orElseThrow(() -> {
            logger.error(LogInfo.toJson(LogLevel.ERROR, "AppUserService.loadUserByUsername", "UsernameNotFoundException", "The username could not be found in the database", "Throw UsernameNotFoundException", username));
            return new UsernameNotFoundException("The username " +
                    username + " could not be found in the database");
        });
    }

    public AppUser getUserByUsername(String username) {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserService.getUserByUsername", "", "", "Getting user by username", username));
        return appUserRepository.findByUsername(username).orElse(null);
    }

    public boolean groupBelongsToUser(AppUser user, long groupId) throws DataNotFoundException {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserService.groupBelongsToUser", "", "", "Checking if group belongs to user", user.getUsername()));
        EntryGroup group = entryGroupRepository.findById(groupId).orElse(null);
        if (group == null) {
            if (logger.isErrorEnabled())
                logger.error(LogInfo.toJson(LogLevel.ERROR, "AppUserService.groupBelongsToUser", "Entrygroup group is null", "EntryGroup could not be found", "Throw DataNotFoundException", user.getUsername()));
            throw new DataNotFoundException("EntryGroup with ID " + groupId + " could not be found");
        }
        boolean belongsToUser = user.getUsername().equals(group.getOwner().getUsername());
        if (logger.isDebugEnabled()) {
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserService.groupBelongsToUser", "", "", "username of user is " + user.getUsername(), user.getUsername()));
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserService.groupBelongsToUser", "", "", "username of groupowner is " + user.getUsername(), user.getUsername()));
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserService.groupBelongsToUser", "", "", "belongsToUser is " + belongsToUser, user.getUsername()));
        }
        return belongsToUser;
    }

    public boolean entryBelongsToUser(AppUser user, long entryId) throws DataNotFoundException {
        if (logger.isDebugEnabled())
            logger.debug(LogInfo.toJson(LogLevel.DEBUG, "AppUserService.entryBelongsToUser", "", "", "Checking if entry belongs to user", user.getUsername()));
        Entry entry = entryRepository.findById(entryId).orElse(null);
        if (entry == null) {
            if (logger.isErrorEnabled())
                logger.error(LogInfo.toJson(LogLevel.ERROR, "AppUserService.entryBelongsToUser", "Entry entry is null", "Entry could not be found", "Throw DataNotFoundException", user.getUsername()));
            throw new DataNotFoundException("Entry with ID " + entryId + " could not be found");
        }
        EntryGroup group = entryGroupRepository.findById(entry.getGroup().getId()).orElse(null);
        if (group == null) {
            if (logger.isErrorEnabled())
                logger.error(LogInfo.toJson(LogLevel.ERROR, "AppUserService.entryBelongsToUser", "Entrytype type is null", "EntryType could not be found", "Throw DataNotFoundException", user.getUsername()));
            throw new DataNotFoundException("EntryType with ID " + entry.getGroup().getId() + " could not be found");
        }
        return groupBelongsToUser(user, group.getId());
    }
}
