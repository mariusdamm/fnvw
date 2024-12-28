package de.marius.fnvw.dto;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MonthDto {

    private Month month;
    private List<EntryGroupDto> intakeGroups = new ArrayList<>();
    private List<EntryGroupDto> spendingGroups = new ArrayList<>();

    public MonthDto(Month month) {
        this.month = month;
    }

    public MonthDto() {
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
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
