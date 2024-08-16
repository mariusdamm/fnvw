package de.marius.fnvw.entity;

import de.marius.fnvw.dto.EntryGroupDto;
import de.marius.fnvw.dto.EntryTypeDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrygroup")
public class EntryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entrygroup_id")
    long id;
    @Column(name = "entrygroup_name", nullable = false)
    String name;
    @Column(name = "entrygroup_month", nullable = false)
    int month;
    @Column(name = "entrygroup_isintake", nullable = false)
    boolean isIntake;
    @OneToMany(mappedBy = "group")
    List<EntryType> entryTypes = new ArrayList<>();
    @ManyToOne()
    @JoinColumn(name = "entrytype_owner")
    AppUser owner;

    public EntryGroup() {
    }

    public EntryGroup(long id, String name, int month, boolean isIntake, AppUser owner) {
        this.isIntake = isIntake;
        this.owner = owner;
        this.name = name;
        this.id = id;
        this.month = month;
    }

    public EntryGroup(String name, int month, boolean isIntake, AppUser owner) {
        this.name = name;
        this.month = month;
        this.isIntake = isIntake;
        this.owner = owner;
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

    public List<EntryType> getEntryTypes() {
        return entryTypes;
    }

    public void setEntryTypes(List<EntryType> entryTypes) {
        this.entryTypes = entryTypes;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public EntryGroupDto toDto() {
        List<EntryTypeDto> types = new ArrayList<>();
        for (EntryType type : entryTypes) {
            types.add(type.toDto());
        }
        return new EntryGroupDto(id, name, month, isIntake, types);
    }

    @Override
    public String toString() {
        return "EntryGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", month=" + month +
                ", isIntake=" + isIntake +
                ", entryTypes=" + entryTypes +
                ", owner=" + owner +
                '}';
    }
}
