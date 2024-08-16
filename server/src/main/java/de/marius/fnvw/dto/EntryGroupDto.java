package de.marius.fnvw.dto;

import de.marius.fnvw.entity.EntryGroup;

import java.util.ArrayList;
import java.util.List;

public class EntryGroupDto {

    long id;
    String name;
    int month;
    boolean isIntake;
    List<EntryTypeDto> entryTypes = new ArrayList<>();

    public EntryGroupDto() {
    }

    public EntryGroupDto(long id, String name, int month, boolean isIntake, List<EntryTypeDto> entryTypes) {
        this.id = id;
        this.name = name;
        this.month = month;
        this.isIntake = isIntake;
        this.entryTypes = entryTypes;
    }

    public EntryGroupDto(String name, int month, boolean isIntake) {
        this.name = name;
        this.month = month;
        this.isIntake = isIntake;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
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

    public boolean getIsIntake() {
        return isIntake;
    }

    public void setIsIntake(boolean isIntake) {
        this.isIntake = isIntake;
    }

    public List<EntryTypeDto> getEntryTypes() {
        return entryTypes;
    }

    public void setEntryTypes(List<EntryTypeDto> entryTypes) {
        this.entryTypes = entryTypes;
    }

    public EntryGroup toEntryGroup() {
        return new EntryGroup(id, name, month, isIntake, null);
    }
}
