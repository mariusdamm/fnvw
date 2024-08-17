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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestEntryGroupService {

    @Autowired
    private EntryGroupService entryGroupService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    private AppUser testUser;

    @BeforeEach
    @Transactional
    void setUp() {
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
    void test_add_valid_entrygroup() throws DataNotFoundException, MissingDataException {
        String testEntryGroupName = "Test Groupname";
        int testEntryGroupMonth = 202409;
        boolean tesEntryGroupIsIntake = true;
        EntryGroupDto group = new EntryGroupDto(testEntryGroupName, testEntryGroupMonth, tesEntryGroupIsIntake);

        EntryGroup result = entryGroupService.addEntryGroup(group, testUser);

        assertNotNull(result);
        assertEquals(result.getName(), testEntryGroupName);
        assertEquals(result.getMonth(), testEntryGroupMonth);
        assertEquals(result.getOwner().getId(), testUser.getId());
    }

    @Test
    @Transactional
    void test_add_invalid_entrygroup(){
        assertThrows(MissingDataException.class, () -> entryGroupService.addEntryGroup(null, testUser));

    }
}
