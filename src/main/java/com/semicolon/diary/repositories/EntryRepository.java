package com.semicolon.diary.repositories;

import com.semicolon.diary.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
}
