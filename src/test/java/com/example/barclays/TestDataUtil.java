package com.example.barclays;

import com.example.barclays.domain.entities.User;

public final class TestDataUtil {

    public TestDataUtil() {
    }

    public static User createTestUser(String userId){
        User testUser = new User();
        testUser.setId(userId);
        testUser.setAddress("1 Street 1");
        testUser.setEmail("user@email.com");
        testUser.setUsername("username");
        testUser.setPassword("password");
        testUser.setFirstnames("Test");
        testUser.setLastnames("User");
        testUser.setPhoneNumber("+448567938376");
        return testUser;
    }

}
