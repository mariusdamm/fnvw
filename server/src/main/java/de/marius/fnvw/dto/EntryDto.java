package de.marius.fnvw.dto;

import de.marius.fnvw.entity.Entry;

public class EntryDto {

    long id;
    String name;
    int value;
    long entryTypeId;

    public EntryDto() {
    }

    public EntryDto(long id, String name, int value, long entryTypeId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.entryTypeId = entryTypeId;
    }

    public EntryDto(String name, int value, long entryTypeId) {
        this.name = name;
        this.value = value;
        this.entryTypeId = entryTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getEntryTypeId() {
        return entryTypeId;
    }

    public void setEntryTypeId(int entryTypeId) {
        this.entryTypeId = entryTypeId;
    }

    public Entry toEntry() {
        return new Entry(id, name, value, null);
    }

    @Override
    public String toString() {
        return "EntryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", entryTypeId=" + entryTypeId +
                '}';
    }
}
