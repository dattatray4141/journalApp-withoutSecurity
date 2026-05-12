package com.journal.service;

import com.journal.entity.Journal;
import com.journal.entity.User;
import com.journal.repository.JournalRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalEntryRepository;

    private final UserService userService;

    @Transactional
    public Journal createJournalEntry(Journal journal, String username){
        User user=userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        journal.setDate(LocalDateTime.now());
        journal.setUser(user);
        Journal savedEntry= journalEntryRepository.save(journal);

        //Suppose here you get error or exception then next step will execute, in that case only journal will update
        //user will not update in that case inconsistency will occur so that's why we use Transactional annotation.4
         /* For Example username will not update journal will update.
        if (true) {
            throw new RuntimeException("Crash after saving journal so Transaction rolled back.");
        }
        user.setUsername("updatedName");
         */
        user.getJournals().add(savedEntry);
        userService.saveUser(user);
        return savedEntry;
    }

    @Transactional(readOnly = true)
    public Journal getJournalEntryById(Long id){

        return journalEntryRepository.findById(id).get();
    }

    public void deleteJournalEntryById(Long id){
        journalEntryRepository.deleteById(id);
    }

}
