package com.semicolon.diary.service;

import com.semicolon.diary.dto.EntryRequest;
import com.semicolon.diary.entity.Entry;
import com.semicolon.diary.exceptions.GenericException;
import com.semicolon.diary.repositories.EntryRepository;
import com.semicolon.diary.service.inter.EntryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EntryServiceImpl implements EntryService {
    @Autowired
    EntryRepository entryRepository;
    @Override
    public String create(EntryRequest entryRequest) {
        Entry entry = new Entry(
                entryRequest.getTitle(),
                entryRequest.getBody()
        );
        entryRepository.save(entry);

       return "Moment created successfully";
    }

    @Override
    public String update(Long id, EntryRequest entryRequest) {
        var foundEntry = entryRepository.findById(id).orElseThrow(()-> new GenericException("Entry not found"));
        foundEntry.setTitle(entryRequest.getTitle());
        foundEntry.setBody(entryRequest.getBody());
        entryRepository.save(foundEntry);
        return "Moment updated successfully";
    }

    @Override
    public void delete(Long id) {
        var foundEntry = entryRepository.findById(id).orElseThrow(()-> new GenericException("Entry not found"));
        entryRepository.delete(foundEntry);
    }

    @Override
    public Entry viewById(Long id) {
        return entryRepository.findById(id).orElseThrow(()-> new GenericException("Entry not found"));
    }

    @Override
    public List<Entry> viewAll() {
        return entryRepository.findAll();
    }
}

