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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryTypeService {

    private final EntryTypeRepository entryTypeRepository;
    private final EntryGroupRepository entryGroupRepository;

    public EntryTypeService(EntryTypeRepository entryTypeRepository, EntryGroupRepository entryGroupRepository) {
        this.entryTypeRepository = entryTypeRepository;
        this.entryGroupRepository = entryGroupRepository;
    }

    public EntryType addEntryType(EntryTypeDto body, AppUser user) throws MissingDataException,
            DataNotFoundException, ForbiddenDataException {
        if (body == null) {
            throw new MissingDataException("Entry body is null");
        }
        EntryType entryType = body.toEntryType();

        if (user == null) {
            throw new DataNotFoundException("User could not be found in the database");
        }
        List<EntryGroup> groups = entryGroupRepository.findByOwner(user);
        EntryGroup group = entryGroupRepository.findById(body.getEntryGroupId()).orElse(null);
        if (group == null) {
            throw new DataNotFoundException("EntryGroup with ID " + body.getEntryGroupId() +
                    " could not be found in the database");
        }
        if (!groups.contains(group)) {
            throw new ForbiddenDataException("EntryType with ID " + body.getEntryGroupId() +
                    " does not belong to user " + user.getName());
        }
        entryType.setGroup(group);

        entryTypeRepository.save(entryType);
        return entryType;
    }
}
