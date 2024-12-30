package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.EntryGroupRepository;
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

import java.time.LocalDateTime;
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
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private AppUser testUser;
    private EntryGroup testGroup;

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
        boolean testGroupIsIntake = true;

        testUser = appUserRepository.save(new AppUser(testUserName, testUserUsername, passwordEncoder.encode(testUserPwd), roles));
        testGroup = entryGroupRepository.save(new EntryGroup(testGroupName, testGroupIsIntake, testUser));
    }

    @Test
    @Transactional
    void test_add_valid_entry() throws DataNotFoundException, MissingDataException, ForbiddenDataException {
        String testEntryName = "test entry";
        int testEntryValue = 1150;
        EntryDto entryDto = new EntryDto(testEntryName, testEntryValue, LocalDateTime.now(), testGroup.getId());

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
