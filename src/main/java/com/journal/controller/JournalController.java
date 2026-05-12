package com.journal.controller;

import com.journal.entity.Journal;
import com.journal.entity.User;
import com.journal.service.JournalService;
import com.journal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalController {

    private final JournalService journalService;

    private final UserService userService;

    public JournalController(JournalService journalService, UserService userService) {
           this.journalService = journalService;
           this.userService = userService;
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> createJournal(@RequestBody Journal journal, @PathVariable String username) {
        try {
            Journal journal1 = journalService.createJournalEntry(journal,username);
            return new ResponseEntity<>(journal1, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getAllJournalEntries(@PathVariable String username) {
        User user=userService.findByUsername(username);
        List<Journal> journals=user.getJournals();
        if(journals!=null && !journals.isEmpty()){
            return new ResponseEntity<>(journals,HttpStatus.OK);
        }
        return new ResponseEntity<>(journals,HttpStatus.FOUND);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable Long id) {
        try {
            Journal journal1 = journalService.getJournalEntryById(id);
            return new ResponseEntity<>(journal1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteJournal(@PathVariable Long id) {
        try {
            Journal journal=journalService.getJournalEntryById(id);
            journalService.deleteJournalEntryById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
