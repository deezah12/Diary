package com.semicolon.diary.service.inter;

import com.semicolon.diary.dto.EntryRequest;
import com.semicolon.diary.entity.Entry;

import java.util.List;

public interface EntryService {
    String create(EntryRequest entryRequest);
    String update(Long id, EntryRequest entryRequest);
    void delete(Long id);
    Entry viewById(Long id);
    List<Entry> viewAll();
}
