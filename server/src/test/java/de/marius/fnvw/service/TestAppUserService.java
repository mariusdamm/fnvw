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
    @Autowired
    private AppUserService appUserService;

    private AppUser testUser1;
    private EntryGroup groupOfUser1;
    private EntryGroup groupOfUser2;

    @BeforeEach
    @Transactional
    void set_up() throws DataNotFoundException, MissingDataException {
        Role role = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String testUserName1 = "Test User1";
        String testUserUsername1 = "Test Username1";
        String testUserPwd1 = "test_pwd1";
        String testUserName2 = "Test User2";
        String testUserUsername2 = "Test Username2";
        String testUserPwd2 = "test_pwd2";

        testUser1 = appUserRepository.save(new AppUser(testUserName1, testUserUsername1, passwordEncoder.encode(testUserPwd1), roles));
        AppUser testUser2 = appUserRepository.save(new AppUser(testUserName2, testUserUsername2, passwordEncoder.encode(testUserPwd2), roles));

        String testEntryGroupName1 = "Test Groupname1";
        int testEntryGroupMonth1 = 202409;
        boolean tesEntryGroupIsIntake1 = true;
        EntryGroupDto dto1 = new EntryGroupDto(testEntryGroupName1, testEntryGroupMonth1, tesEntryGroupIsIntake1);
        groupOfUser1 = entryGroupService.addEntryGroup(dto1, testUser1);

        String testEntryGroupName2 = "Test Groupname2";
        int testEntryGroupMonth2 = 202409;
        boolean tesEntryGroupIsIntake2 = false;
        EntryGroupDto dto2 = new EntryGroupDto(testEntryGroupName2, testEntryGroupMonth2, tesEntryGroupIsIntake2);
        groupOfUser2 = entryGroupService.addEntryGroup(dto2, testUser2);
    }

    @Test
    @Transactional
    void test_valid_group_belongs_to_user() throws DataNotFoundException {
        boolean result = appUserService.groupBelongsToUser(testUser1, groupOfUser1.getId());

        assertTrue(result);
    }

    @Test
    @Transactional
    void test_invalid_group_belongs_to_user() throws DataNotFoundException {
        boolean result = appUserService.groupBelongsToUser(testUser1, groupOfUser2.getId());

        assertFalse(result);
    }
}
