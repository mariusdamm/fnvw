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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    private final EntryTypeRepository entryTypeRepository;
    private final EntryGroupRepository entryGroupRepository;

    public EntryService(
            EntryRepository entryRepository,
            EntryTypeRepository entryTypeRepository,
            EntryGroupRepository entryGroupRepository
    ) {
        this.entryRepository = entryRepository;
        this.entryTypeRepository = entryTypeRepository;
        this.entryGroupRepository = entryGroupRepository;
    }

    public Entry addEntry(
            EntryDto body,
            AppUser user
    ) throws DataNotFoundException,
            MissingDataException,
            ForbiddenDataException
    {
        if (body == null) {
            throw new MissingDataException("Entry body is null");
        }
        Entry entry = body.toEntry();

        if (user == null) {
            throw new DataNotFoundException("User could not be found in the database");
        }
        List<EntryGroup> groups = entryGroupRepository.findByOwner(user);
        List<EntryType> types = new ArrayList<>();
        for (EntryGroup group : groups) {
            types.addAll(entryTypeRepository.findByGroup(group));
        }

        EntryType type = entryTypeRepository.findById(body.getEntryTypeId()).orElse(null);
        if (type == null) {
            throw new DataNotFoundException("EntryType with ID " + body.getEntryTypeId() +
                    " could not be found in the database");
        }
        if (!types.contains(type)) {
            throw new ForbiddenDataException("EntryType with ID " + body.getEntryTypeId() +
                    " does not belong to user " + user.getName());
        }
        entry.setType(type);

        entryRepository.save(entry);
        return entry;
    }
}
