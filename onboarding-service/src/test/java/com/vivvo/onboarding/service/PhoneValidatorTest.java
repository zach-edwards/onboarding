package com.vivvo.onboarding.service;

import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.repository.PhoneRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PhoneValidatorTest {


    private PhoneValidator phoneValidator;
    private PhoneRepository phoneRepository;

    @Before
    public void init() {
        phoneRepository = getMockPhoneRepository();
        phoneValidator = new PhoneValidator(phoneRepository);
    }

    @Test
    public void testValidPhoneNumberValidation_shouldSucceed(){
        PhoneDto createdPhone = getValidPhoneDto();
        Map<String, String> errors = phoneValidator.validatePhoneNumber(createdPhone);

        assertEquals(0, errors.size());
    }

    @Test
    public void testInvalidPhoneNumber_shouldFail(){
        PhoneDto createdPhone = getValidPhoneDto().setPhoneNumber("1234567890123");
        Map<String, String> errors = phoneValidator.validatePhoneNumber(createdPhone);

        assertEquals(1, errors.size());
        assertTrue(errors.containsKey("phoneNumber"));
        assertEquals(PhoneValidator.INVALID_PHONE_NUMBER, errors.get("phoneNumber"));
    }


    private PhoneDto getValidPhoneDto() {
        return new PhoneDto()
                .setUserId(UUID.randomUUID())
                .setPhoneNumber("3065410371");
    }

    private PhoneRepository getMockPhoneRepository() {
        return mock(PhoneRepository.class);
    }

}
