package com.example.soba2clean.repository;

import com.example.soba2clean.model.Cleaner;
import com.example.soba2clean.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CleanerRepository extends JpaRepository<Cleaner, Long> {
    List<Cleaner> findByUser(User user);

    Cleaner findCleanerByUser(User user);
}
