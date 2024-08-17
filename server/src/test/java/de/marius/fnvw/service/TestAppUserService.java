package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.dto.EntryGroupDto;
import de.marius.fnvw.dto.EntryTypeDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.entity.EntryType;
import de.marius.fnvw.entity.Role;
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
    @Autowired
    private EntryTypeService entryTypeService;

    private AppUser testUser1;
    private EntryGroup groupOfUser1;
    private EntryGroup groupOfUser2;
    private EntryType typeOfUser1;
    private EntryType typeOfUser2;

    @BeforeEach
    @Transactional
    void set_up() throws DataNotFoundException, MissingDataException, ForbiddenDataException {
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
        EntryGroupDto groupDto1 = new EntryGroupDto(testEntryGroupName1, testEntryGroupMonth1, tesEntryGroupIsIntake1);
        String testEntryGroupName2 = "Test Groupname2";
        int testEntryGroupMonth2 = 202409;
        boolean tesEntryGroupIsIntake2 = false;
        EntryGroupDto groupDto2 = new EntryGroupDto(testEntryGroupName2, testEntryGroupMonth2, tesEntryGroupIsIntake2);

        groupOfUser1 = entryGroupService.addEntryGroup(groupDto1, testUser1);
        groupOfUser2 = entryGroupService.addEntryGroup(groupDto2, testUser2);

        String testEntryTypeName1 = "test entrytype1";
        EntryTypeDto typeDto1 = new EntryTypeDto(groupOfUser1.getId(), testEntryTypeName1);
        String testEntryTypeName2 = "test entrytype2";
        EntryTypeDto typeDto2 = new EntryTypeDto(groupOfUser2.getId(), testEntryTypeName2);

        typeOfUser1 = entryTypeService.addEntryType(typeDto1, testUser1);
        typeOfUser2 = entryTypeService.addEntryType(typeDto2, testUser2);
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

    @Test
    @Transactional
    void test_valid_type_belongs_to_user() throws DataNotFoundException {
        boolean result = appUserService.typeBelongsToUser(testUser1, typeOfUser1.getId());
        assertTrue(result);
    }

    @Test
    @Transactional
    void test_invalid_type_belongs_to_user() throws DataNotFoundException {
        boolean result = appUserService.typeBelongsToUser(testUser1, typeOfUser2.getId());
        assertFalse(result);
    }
}
