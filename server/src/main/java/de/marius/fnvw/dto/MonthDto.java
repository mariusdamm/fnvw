package de.marius.fnvw.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MonthDto {

    private LocalDateTime month;
    private List<EntryGroupDto> intakeGroups = new ArrayList<>();
    private List<EntryGroupDto> spendingGroups = new ArrayList<>();

    public MonthDto(LocalDateTime month) {
        this.month = month;
    }

    public MonthDto() {
    }

    public LocalDateTime getMonth() {
        return month;
    }

    public void setMonth(LocalDateTime month) {
        this.month = month;
    }

    public List<EntryGroupDto> getIntakeGroups() {
        return intakeGroups;
    }

    public void setIntakeGroups(List<EntryGroupDto> intakeGroups) {
        this.intakeGroups = intakeGroups;
    }

    public List<EntryGroupDto> getSpendingGroups() {
        return spendingGroups;
    }

    public void setSpendingGroups(List<EntryGroupDto> spendingGroups) {
        this.spendingGroups = spendingGroups;
    }

    public void addIntakeGroup(EntryGroupDto group) {
        intakeGroups.add(group);
    }

    public void addSpendingGroup(EntryGroupDto group) {
        spendingGroups.add(group);
    }
}
