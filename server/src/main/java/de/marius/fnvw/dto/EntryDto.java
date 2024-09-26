package de.marius.fnvw.dto;

import de.marius.fnvw.entity.Entry;

public class EntryDto {

    long id;
    String name;
    int value;
    long entryGroupId;

    public EntryDto() {
    }

    public EntryDto(long id, String name, int value, long entryTypeId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.entryGroupId = entryTypeId;
    }

    public EntryDto(String name, int value, long entryTypeId) {
        this.name = name;
        this.value = value;
        this.entryGroupId = entryTypeId;
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

    public long getEntryGroupId() {
        return entryGroupId;
    }

    public void setEntryGroupId(int entryGroupId) {
        this.entryGroupId = entryGroupId;
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
                ", entryGroupId=" + entryGroupId +
                '}';
    }
}
