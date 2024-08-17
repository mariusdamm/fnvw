package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dao.EntryRepository;
import de.marius.fnvw.dao.EntryTypeRepository;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Entry;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.entity.EntryType;
import de.marius.fnvw.exception.DataNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final EntryGroupRepository entryGroupRepository;
    private final EntryTypeRepository entryTypeRepository;
    private final EntryRepository entryRepository;

    public AppUserService(AppUserRepository appUserRepository, EntryGroupRepository entryGroupRepository,
                          EntryTypeRepository entryTypeRepository, EntryRepository entryRepository) {
        this.appUserRepository = appUserRepository;
        this.entryGroupRepository = entryGroupRepository;
        this.entryTypeRepository = entryTypeRepository;
        this.entryRepository = entryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username).orElseThrow();
    }

    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByUsername(username).orElse(null);
    }

    public boolean groupBelongsToUser(AppUser user, long groupId) throws DataNotFoundException {
        EntryGroup group = entryGroupRepository.findById(groupId).orElse(null);
        if (group == null)
            throw new DataNotFoundException("EntryGroup with ID " + groupId + " could not be found");
        return user.getUsername().equals(group.getOwner().getUsername());
    }

    public boolean typeBelongsToUser(AppUser user, long typeId) throws DataNotFoundException {
        EntryType type = entryTypeRepository.findById(typeId).orElse(null);
        if (type == null)
            throw new DataNotFoundException("EntryType with ID " + typeId + " could not be found");
        EntryGroup group = entryGroupRepository.findById(type.getGroup().getId()).orElse(null);
        if (group == null)
            throw new DataNotFoundException("EntryGroup with ID " + type.getGroup().getId() + " could not be found");
        return groupBelongsToUser(user, group.getId());
    }

    public boolean entryBelongsToUser(AppUser user, long entryId) throws DataNotFoundException {
        Entry entry = entryRepository.findById(entryId).orElse(null);
        if (entry == null)
            throw new DataNotFoundException("Entry with ID " + entryId + " could not be found");
        EntryType type = entryTypeRepository.findById(entry.getType().getId()).orElse(null);
        if (type == null)
            throw new DataNotFoundException("EntryType with ID " + entry.getType().getId() + " could not be found");
        return typeBelongsToUser(user, type.getId());
    }
}
