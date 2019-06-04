package com.vivvo.onboarding.service;


import com.vivvo.onboarding.UserDto;
import com.vivvo.onboarding.repository.UserRepository;
import com.vivvo.onboarding.service.user_service.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PhoneValidatorTest {
/*
    private UserValidator userValidator;

    private UserRepository userRepository;

    @Before
    public void init() {
        userRepository = getMockUserRepository();
        userValidator = new UserValidator(userRepository);
    }


    @Test
    public void testFirstNameRequired() {
        UserDto dto = getValidUserDto()
                .setFirstName(null);

        Map<String, String> errors = userValidator.validate(dto);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("firstName"));
        assertEquals(UserValidator.FIRST_NAME_REQUIRED, errors.get("firstName"));
    }

    @Test
    public void testFirstNameGreaterThan50Characters() {
        UserDto dto = getValidUserDto()
                .setFirstName("12345678901234567890123456789012345678901234567890X");

        Map<String, String> errors = userValidator.validate(dto);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("firstName"));
        assertEquals(UserValidator.FIRST_NAME_LT_50, errors.get("firstName"));
    }

    @Test
    public void testLastNameRequired() {
        UserDto dto = getValidUserDto()
                .setLastName(null);

        Map<String, String> errors = userValidator.validate(dto);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("lastName"));
        assertEquals(UserValidator.LAST_NAME_REQUIRED, errors.get("lastName"));
    }


    @Test
    public void testUsernameTaken() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        UserDto dto = getValidUserDto();

        Map<String, String> errors = userValidator.validate(dto);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("username"));
        assertEquals(UserValidator.USERNAME_TAKEN, errors.get("username"));
    }

    private UserDto getValidUserDto() {
        return new UserDto()
                .setFirstName("Tim")
                .setLastName("Dodd")
                .setUsername("doddt");
    }

    private UserRepository getMockUserRepository() {
        return mock(UserRepository.class);
    }*/
}
