package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.EntryGroupRepository;
import de.marius.fnvw.dao.RoleRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestEntryTypeService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EntryGroupRepository entryGroupRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntryTypeService entryTypeService;

    private AppUser testUser;
    private EntryGroup testGroup;

    @BeforeEach
    @Transactional
    void setUp(){
        Role role = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String testUserName = "Test User";
        String testUserUsername = "Test Username";
        String testUserPwd = "test_pwd";

        String testGroupName = "test group";
        int testGroupMonth = 202407;
        boolean testGroupIsIntake = true;

        testUser = appUserRepository.save(new AppUser(testUserName, testUserUsername, passwordEncoder.encode(testUserPwd), roles));
        testGroup = entryGroupRepository.save(new EntryGroup(testGroupName, testGroupMonth, testGroupIsIntake, testUser));
    }

    @Test
    @Transactional
    void test_add_valid_entrytype() throws DataNotFoundException, ForbiddenDataException, MissingDataException {
        String testEntryTypeName = "test entrytype";
        EntryTypeDto entryTypeDto = new EntryTypeDto(testGroup.getId(), testEntryTypeName);

        EntryType result = entryTypeService.addEntryType(entryTypeDto, testUser);

        assertNotNull(result);
        assertEquals(result.getGroup().getId(), testGroup.getId());
        assertEquals(testEntryTypeName, result.getName());
    }

    @Test
    @Transactional
    void test_add_invalid_entrytype(){
        assertThrows(MissingDataException.class, () -> entryTypeService.addEntryType(null, testUser));
    }
}
