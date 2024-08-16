package de.marius.fnvw.service;

import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dto.EntryGroupDto;
import de.marius.fnvw.dto.MonthDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.MissingDataException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryGroupService {

    private final EntryGroupRepository entryGroupRepository;

    public EntryGroupService(EntryGroupRepository entryGroupRepository) {
        this.entryGroupRepository = entryGroupRepository;
    }

    public EntryGroup addEntryGroup(EntryGroupDto body, AppUser user) throws MissingDataException, DataNotFoundException {
        if (body == null) {
            throw new MissingDataException("Entry body is null");
        }
        EntryGroup entryGroup = body.toEntryGroup();

        if (user == null) {
            throw new DataNotFoundException("User could not be found in the database");
        }
        entryGroup.setOwner(user);

        entryGroupRepository.save(entryGroup);
        return entryGroup;
    }

    public MonthDto getEntryGroupsOfUserInMonth(AppUser user, int month) {
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

        if(dto.getIntakeGroups().isEmpty() && dto.getSpendingGroups().isEmpty())
            return null;
        return dto;
    }

    public EntryGroup updateEntryGroup(EntryGroupDto body) throws DataNotFoundException {
        EntryGroup group = entryGroupRepository.findById(body.getId()).orElse(null);
        if (group == null)
            throw new DataNotFoundException("EntryGroup with id " + body.getId() + " could not be found");
        group.setName(body.getName());
        group.setIsIntake(body.getIsIntake());
        return entryGroupRepository.save(group);
    }
}
