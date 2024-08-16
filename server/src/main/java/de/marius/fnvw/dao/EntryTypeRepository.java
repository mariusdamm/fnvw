package de.marius.fnvw.dao;

import de.marius.fnvw.entity.EntryGroup;
import de.marius.fnvw.entity.EntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryTypeRepository extends JpaRepository<EntryType, Long> {

    List<EntryType> findByGroup(EntryGroup group);
}
