package com.semicolon.diary.service;

import com.semicolon.diary.dto.EntryRequest;
import com.semicolon.diary.entity.Entry;
import com.semicolon.diary.exceptions.GenericException;
import com.semicolon.diary.repositories.EntryRepository;
import com.semicolon.diary.service.inter.EntryService;
import com.semicolon.diary.service.inter.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EntryServiceImpl implements EntryService {
    @Autowired
    EntryRepository entryRepository;
    @Autowired
    UserService userService;

    @Override
    public String create(String emailAddress, EntryRequest entryRequest) {
       var user = userService.getByEmailAddress(emailAddress).get();
        Entry entry = new Entry(
                entryRequest.getTitle(),
                entryRequest.getBody(),
                user
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
    public String delete(Long id) {
        var foundEntry = entryRepository.findById(id).orElseThrow(()-> new GenericException("Entry not found"));
        entryRepository.delete(foundEntry);
        return "Deleted successfully";
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

