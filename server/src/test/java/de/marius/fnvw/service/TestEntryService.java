package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dao.EntryTypeRepository;
import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.dto.EntryDto;
import de.marius.fnvw.entity.*;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.ForbiddenDataException;
import de.marius.fnvw.exception.MissingDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestEntryService {

    @Autowired
    private EntryService entryService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EntryGroupRepository entryGroupRepository;
    @Autowired
    private EntryTypeRepository entryTypeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private AppUser testUser;
    private EntryType testType;

    @BeforeEach
    @Transactional
    void setUp() {
        Role role = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String testUserName = "Test User";
        String testUserUsername = "Test Username";
        String testUserPwd = "test_pwd";

        String testGroupName = "test group";
        int testGroupMonth = 202407;
        boolean testGroupIsIntake = true;

        String testTypeName = "Test typppe";

        testUser = appUserRepository.save(new AppUser(testUserName, testUserUsername, passwordEncoder.encode(testUserPwd), roles));
        EntryGroup testGroup = entryGroupRepository.save(new EntryGroup(testGroupName, testGroupMonth, testGroupIsIntake, testUser));
        testType = entryTypeRepository.save(new EntryType(testTypeName, testGroup));
    }

    @Test
    @Transactional
    void test_add_valid_entry() throws DataNotFoundException, MissingDataException, ForbiddenDataException {
        String testEntryName = "test entry";
        int testEntryValue = 1150;
        EntryDto entryDto = new EntryDto(testEntryName, testEntryValue, testType.getId());

        Entry result = entryService.addEntry(entryDto, testUser);

        assertNotNull(result);
        assertEquals(testEntryName, result.getName());
        assertEquals(testEntryValue, result.getValue());
    }

    @Test
    @Transactional
    void test_add_invalid_entry() {
        assertThrows(MissingDataException.class, () -> entryService.addEntry(null, testUser));
    }
}
