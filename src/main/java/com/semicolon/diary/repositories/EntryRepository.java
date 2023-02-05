package com.semicolon.diary.repositories;

import com.semicolon.diary.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
}
