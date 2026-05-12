package com.journal.controller;

import com.journal.entity.User;
import com.journal.service.EmailService;
import com.journal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping
    public void create(@RequestBody User user) {

         userService.saveUser(user);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<?> update(@RequestBody  User user, @PathVariable String username) {
        User user1=userService.findByUsername(username);
        if(user1!=null) {
            user1.setUsername(user.getUsername());
            user1.setPassword(user.getPassword());
            userService.saveUser(user1);//no return tpye
            return new ResponseEntity<>(HttpStatus.OK);
        }
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<List<User>> (userService.getAll(), HttpStatus.OK);
    }

    //Instead of creating separate email controller I'm creating endpoint here to sends the mail
    //send mail from my account to acc .....
    //We can also send mail to specific user by getting userdetails and send them.
    @GetMapping ("sendmail")
    public ResponseEntity<?> sendEmail(@RequestParam String to,@RequestParam String subject,@RequestParam String text) {
      try{
          emailService.sendEmail(to,subject,text);
          return new ResponseEntity<>("Email sent successfully",HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>("Email sent failed",HttpStatus.BAD_REQUEST);
      }
    }

}
