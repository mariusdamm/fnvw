package de.marius.fnvw.entity;

import de.marius.fnvw.dto.EntryDto;
import jakarta.persistence.*;

@Entity
@Table(name = "entry")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    long id;
    @Column(name = "entry_name")
    String name;
    @Column(name = "entry_value", nullable = false)
    int value;
    @ManyToOne()
    @JoinColumn(name = "entry_entrygroup", nullable = false)
    EntryGroup group;

    public Entry() {
    }

    public Entry(long id, String name, int value, EntryGroup group) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.group = group;
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
        return new EntryDto(id, name, value, group.getId());
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", group=" + group +
                '}';
    }
}
