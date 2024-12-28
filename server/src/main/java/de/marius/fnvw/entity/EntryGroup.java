package de.marius.fnvw.entity;

import de.marius.fnvw.dto.EntryDto;
import de.marius.fnvw.dto.EntryGroupDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrygroup")
public class EntryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entrygroup_id")
    private long id;
    @Column(name = "entrygroup_name", nullable = false)
    private String name;
    @Column(name = "entrygroup_isintake", nullable = false)
    private boolean isIntake;
    @OneToMany(mappedBy = "group")
    private List<Entry> entries = new ArrayList<>();
    @ManyToOne()
    @JoinColumn(name = "entrytype_owner")
    private AppUser owner;

    public EntryGroup() {
    }

    public EntryGroup(long id, String name, boolean isIntake, AppUser owner) {
        this.isIntake = isIntake;
        this.owner = owner;
        this.name = name;
        this.id = id;
    }

    public EntryGroup(String name, boolean isIntake, AppUser owner) {
        this.name = name;
        this.isIntake = isIntake;
        this.owner = owner;
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

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public EntryGroupDto toDto() {
        List<EntryDto> entryDtos = new ArrayList<>();
        for (Entry entry : entries) {
            entryDtos.add(entry.toDto());
        }
        return new EntryGroupDto(id, name, isIntake, entryDtos);
    }

    public EntryGroupDto toMonthGroupDto() {
        List<EntryDto> entryDtos = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Entry entry : entries) {
            if (entry.getDate().getMonth().equals(now.getMonth()))
                entryDtos.add(entry.toDto());
        }
        return new EntryGroupDto(id, name, isIntake, entryDtos);
    }

    @Override
    public String toString() {
        return "EntryGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isIntake=" + isIntake +
                ", entries=" + entries +
                ", owner=" + owner +
                '}';
    }
}
