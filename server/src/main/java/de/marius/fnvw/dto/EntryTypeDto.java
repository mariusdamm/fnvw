package de.marius.fnvw.dto;

import de.marius.fnvw.entity.EntryType;

import java.util.ArrayList;
import java.util.List;

public class EntryTypeDto {

    long id;
    String name;
    List<EntryDto> entries = new ArrayList<>();
    long entryGroupId;

    public EntryTypeDto() {
    }

    public EntryTypeDto(long id, String name, List<EntryDto> entries, long entryGroupId) {
        this.id = id;
        this.name = name;
        this.entries = entries;
        this.entryGroupId = entryGroupId;
    }

    public EntryTypeDto(long entryGroupId, String name) {
        this.entryGroupId = entryGroupId;
        this.name = name;
    }

    public long getEntryGroupId() {
        return entryGroupId;
    }

    public void setEntryGroupId(long entryGroupId) {
        this.entryGroupId = entryGroupId;
    }

    public List<EntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<EntryDto> entries) {
        this.entries = entries;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntryType toEntryType() {
        return new EntryType(id, name, null);
    }
}
