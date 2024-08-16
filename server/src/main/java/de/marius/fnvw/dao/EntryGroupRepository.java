package de.marius.fnvw.dao;

import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.EntryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryGroupRepository extends JpaRepository<EntryGroup, Long> {

    List<EntryGroup> findByOwner(AppUser owner);
}
