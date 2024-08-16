package de.marius.fnvw.entity;

import de.marius.fnvw.dto.EntryDto;
import de.marius.fnvw.dto.EntryTypeDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrytype")
public class EntryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entrytype_id")
    long id;
    @Column(name = "entrytype_name", nullable = false)
    String name;
    @OneToMany(mappedBy = "type")
    List<Entry> entries = new ArrayList<>();
    @ManyToOne()
    @JoinColumn(name = "entrytype_group")
    EntryGroup group;

    public EntryType() {
    }

    public EntryType(long id, String name, EntryGroup group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public EntryType(String name, EntryGroup group) {
        this.group = group;
        this.name = name;
    }

    public EntryGroup getGroup() {
        return group;
    }

    public void setGroup(EntryGroup group) {
        this.group = group;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
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

    public EntryTypeDto toDto() {
        List<EntryDto> dtos = new ArrayList<>();
        for (Entry entry : entries) {
            dtos.add(entry.toDto());
        }
        return new EntryTypeDto(id, name, dtos, group.getId());
    }

    @Override
    public String toString() {
        return "EntryType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", entries=" + entries +
                ", group=" + group +
                '}';
    }
}
