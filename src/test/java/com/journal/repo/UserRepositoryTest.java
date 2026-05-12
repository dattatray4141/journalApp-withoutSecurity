package com.journal.repo;

import com.journal.entity.User;
import com.journal.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindBySentimentAnalysisTrueAndEmail() {

        // Arrange just for testing purpose
        User user = new User();
        user.setUsername("pratik");
        user.setEmail("pratik4@gmail.com");
        user.setSentimentAnalysis(true);

        userRepository.save(user);

        // Act
        List<User> users =
                userRepository.findBySentimentAnalysisTrueAndEmail(
                        "pratik4@gmail.com"
                );

        // Assert
        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());

        Assertions.assertEquals(
                "pratik4@gmail.com",
                users.get(0).getEmail()
        );
    }
}