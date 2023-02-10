package com.semicolon.diary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.semicolon.diary.dto.EntryRequest;
import com.semicolon.diary.entity.Entry;
import com.semicolon.diary.entity.User;
import com.semicolon.diary.exceptions.GenericException;
import com.semicolon.diary.repositories.EntryRepository;
import com.semicolon.diary.service.inter.UserService;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EntryServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EntryServiceImplTest {
    @MockBean
    private EntryRepository entryRepository;

    @Autowired
    private EntryServiceImpl entryServiceImpl;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link EntryServiceImpl#create(String, EntryRequest)}
     */
    @Test
    void testCreate() {
        when(entryRepository.save((Entry) any())).thenReturn(new Entry());
        when(userService.getByEmailAddress((String) any())).thenReturn(new User("Jane", "Doe", "dija@gmail.com", "Iloveyou1!"));

        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setBody("Not all who wander are lost");
        entryRequest.setTitle("Dr");
        assertEquals("Moment created successfully", entryServiceImpl.create("dija@gmail.com", entryRequest));
        verify(entryRepository).save((Entry) any());
        verify(userService).getByEmailAddress((String) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#create(String, EntryRequest)}
     */
    @Test
    void testCreate2() {
        when(entryRepository.save((Entry) any())).thenReturn(new Entry());
        when(userService.getByEmailAddress((String) any())).thenThrow(new GenericException("An error occurred"));

        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setBody("Not all who wander are lost");
        entryRequest.setTitle("Dr");
        assertThrows(GenericException.class, () -> entryServiceImpl.create("dija@gmail.com", entryRequest));
        verify(userService).getByEmailAddress((String) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#update(Long, EntryRequest)}
     */
    @Test
    void testUpdate() {
        when(entryRepository.save((Entry) any())).thenReturn(new Entry());
        when(entryRepository.findById((Long) any())).thenReturn(Optional.of(new Entry()));

        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setBody("Not all who wander are lost");
        entryRequest.setTitle("Dr");
        assertEquals("Moment updated successfully", entryServiceImpl.update(123L, entryRequest));
        verify(entryRepository).save((Entry) any());
        verify(entryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#update(Long, EntryRequest)}
     */
    @Test
    void testUpdate2() {
        when(entryRepository.save((Entry) any())).thenThrow(new GenericException("An error occurred"));
        when(entryRepository.findById((Long) any())).thenReturn(Optional.of(new Entry()));

        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setBody("Not all who wander are lost");
        entryRequest.setTitle("Dr");
        assertThrows(GenericException.class, () -> entryServiceImpl.update(123L, entryRequest));
        verify(entryRepository).save((Entry) any());
        verify(entryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#update(Long, EntryRequest)}
     */
    @Test
    void testUpdate3() {
        Entry entry = mock(Entry.class);
        doNothing().when(entry).setBody((String) any());
        doNothing().when(entry).setTitle((String) any());
        Optional<Entry> ofResult = Optional.of(entry);
        when(entryRepository.save((Entry) any())).thenReturn(new Entry());
        when(entryRepository.findById((Long) any())).thenReturn(ofResult);

        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setBody("Not all who wander are lost");
        entryRequest.setTitle("Dr");
        assertEquals("Moment updated successfully", entryServiceImpl.update(123L, entryRequest));
        verify(entryRepository).save((Entry) any());
        verify(entryRepository).findById((Long) any());
        verify(entry).setBody((String) any());
        verify(entry).setTitle((String) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#update(Long, EntryRequest)}
     */
    @Test
    void testUpdate4() {
        when(entryRepository.save((Entry) any())).thenReturn(new Entry());
        when(entryRepository.findById((Long) any())).thenReturn(Optional.empty());

        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setBody("Not all who wander are lost");
        entryRequest.setTitle("Dr");
        assertThrows(GenericException.class, () -> entryServiceImpl.update(123L, entryRequest));
        verify(entryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#update(Long, EntryRequest)}
     */
    @Test
    void testUpdate5() {
        Entry entry = mock(Entry.class);
        doThrow(new GenericException("An error occurred")).when(entry).setBody((String) any());
        doThrow(new GenericException("An error occurred")).when(entry).setTitle((String) any());
        Optional<Entry> ofResult = Optional.of(entry);
        when(entryRepository.save((Entry) any())).thenReturn(new Entry());
        when(entryRepository.findById((Long) any())).thenReturn(ofResult);

        EntryRequest entryRequest = new EntryRequest();
        entryRequest.setBody("Not all who wander are lost");
        entryRequest.setTitle("Dr");
        assertThrows(GenericException.class, () -> entryServiceImpl.update(123L, entryRequest));
        verify(entryRepository).findById((Long) any());
        verify(entry).setTitle((String) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#delete(Long)}
     */
    @Test
    void testDelete() {
        doNothing().when(entryRepository).delete((Entry) any());
        when(entryRepository.findById((Long) any())).thenReturn(Optional.of(new Entry()));
        assertEquals("Deleted successfully", entryServiceImpl.delete(123L));
        verify(entryRepository).findById((Long) any());
        verify(entryRepository).delete((Entry) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#delete(Long)}
     */
    @Test
    void testDelete2() {
        doThrow(new GenericException("An error occurred")).when(entryRepository).delete((Entry) any());
        when(entryRepository.findById((Long) any())).thenReturn(Optional.of(new Entry()));
        assertThrows(GenericException.class, () -> entryServiceImpl.delete(123L));
        verify(entryRepository).findById((Long) any());
        verify(entryRepository).delete((Entry) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#delete(Long)}
     */
    @Test
    void testDelete3() {
        doNothing().when(entryRepository).delete((Entry) any());
        when(entryRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(GenericException.class, () -> entryServiceImpl.delete(123L));
        verify(entryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#viewById(Long)}
     */
    @Test
    void testViewById() {
        Entry entry = new Entry();
        when(entryRepository.findById((Long) any())).thenReturn(Optional.of(entry));
        assertSame(entry, entryServiceImpl.viewById(123L));
        verify(entryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#viewById(Long)}
     */
    @Test
    void testViewById2() {
        when(entryRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(GenericException.class, () -> entryServiceImpl.viewById(123L));
        verify(entryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#viewById(Long)}
     */
    @Test
    void testViewById3() {
        when(entryRepository.findById((Long) any())).thenThrow(new GenericException("An error occurred"));
        assertThrows(GenericException.class, () -> entryServiceImpl.viewById(123L));
        verify(entryRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link EntryServiceImpl#viewAll()}
     */
    @Test
    void testViewAll() {
        ArrayList<Entry> entryList = new ArrayList<>();
        when(entryRepository.findAll()).thenReturn(entryList);
        List<Entry> actualViewAllResult = entryServiceImpl.viewAll();
        assertSame(entryList, actualViewAllResult);
        assertTrue(actualViewAllResult.isEmpty());
        verify(entryRepository).findAll();
    }

    /**
     * Method under test: {@link EntryServiceImpl#viewAll()}
     */
    @Test
    void testViewAll2() {
        when(entryRepository.findAll()).thenThrow(new GenericException("An error occurred"));
        assertThrows(GenericException.class, () -> entryServiceImpl.viewAll());
        verify(entryRepository).findAll();
    }
}

