package de.marius.fnvw.dto;

import de.marius.fnvw.entity.Entry;

import java.time.LocalDateTime;

public class EntryDto {

  long id;
  String name;
  int value;
  LocalDateTime date;
  long entryGroupId;

  public EntryDto() {
  }

  public EntryDto(long id, String name, int value, LocalDateTime date, long entryGroupId) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.date = date;
    this.entryGroupId = entryGroupId;
  }

  public EntryDto(String name, int value, LocalDateTime date, long entryGroupId) {
    this.name = name;
    this.value = value;
    this.date = date;
    this.entryGroupId = entryGroupId;
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

  public void setEntryGroupId(long entryGroupId) {
    this.entryGroupId = entryGroupId;
  }

  public void setEntryGroupId(int entryGroupId) {
    this.entryGroupId = entryGroupId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public Entry toEntry() {
    return new Entry(id, name, value, date, null);
  }

  @Override
  public String toString() {
    return "EntryDto{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", value=" + value +
      ", date=" + date +
      ", entryGroupId=" + entryGroupId +
      '}';
  }
}
