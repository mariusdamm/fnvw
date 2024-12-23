package de.marius.fnvw.entity;

import de.marius.fnvw.dto.EntryDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entry")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private long id;
    @Column(name = "entry_name")
    private String name;
    @Column(name = "entry_value", nullable = false)
    private int value;
    @Column(name = "entry_date", nullable = false)
    private LocalDateTime date;
    @ManyToOne()
    @JoinColumn(name = "entry_entrygroup", nullable = false)
    private EntryGroup group;

    public Entry() {
    }

    public Entry(long id, String name, int value, LocalDateTime date, EntryGroup group) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.date = date;
        this.group = group;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public EntryGroup getGroup() {
        return group;
    }

    public void setGroup(EntryGroup group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public EntryDto toDto(){
        return new EntryDto(id, name, value, date, group.getId());
    }

    @Override
    public String toString() {
        return "Entry{" +
          "id=" + id +
          ", name='" + name + '\'' +
          ", value=" + value +
          ", date=" + date +
          ", group=" + group +
          '}';
    }
}
