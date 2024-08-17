package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.dto.EntryGroupDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.entity.Role;
import de.marius.fnvw.exception.DataNotFoundException;
import de.marius.fnvw.exception.MissingDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TestAppUserService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntryGroupService entryGroupService;

    private AppUser testUser;
    @Autowired
    private AppUserService appUserService;

    @BeforeEach
    @Transactional
    void set_up() {
        Role role = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String testUserName = "Test User";
        String testUserUsername = "Test Username";
        String testUserPwd = "test_pwd";

        testUser = appUserRepository.save(new AppUser(testUserName, testUserUsername, passwordEncoder.encode(testUserPwd), roles));
    }

    @Test
    @Transactional
    void test_valid_group_belongs_to_user() throws DataNotFoundException, MissingDataException {
        String testEntryGroupName = "Test Groupname";
        int testEntryGroupMonth = 202409;
        boolean tesEntryGroupIsIntake = true;
        EntryGroupDto dto = new EntryGroupDto(testEntryGroupName, testEntryGroupMonth, tesEntryGroupIsIntake);
        EntryGroup group = entryGroupService.addEntryGroup(dto, testUser);

        boolean result = appUserService.groupBelongsToUser(testUser, group.getId());

        assertTrue(result);
    }

    @Test
    @Transactional
    void test_invalid_group_belongs_to_user() throws DataNotFoundException, MissingDataException {
        Role role = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        String testUserName = "Test User2";
        String testUserUsername = "Test Username2";
        String testUserPwd = "test_pwd2";
        AppUser testUser2 = appUserRepository.save(new AppUser(testUserName, testUserUsername, passwordEncoder.encode(testUserPwd), roles));

        String testEntryGroupName = "Test Groupname";
        int testEntryGroupMonth = 202409;
        boolean tesEntryGroupIsIntake = true;
        EntryGroupDto dto = new EntryGroupDto(testEntryGroupName, testEntryGroupMonth, tesEntryGroupIsIntake);
        EntryGroup group = entryGroupService.addEntryGroup(dto, testUser2);

        boolean result = appUserService.groupBelongsToUser(testUser, group.getId());

        assertFalse(result);
    }
}
