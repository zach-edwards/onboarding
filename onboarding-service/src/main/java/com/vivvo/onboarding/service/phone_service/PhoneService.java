package com.vivvo.onboarding.service.phone_service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vivvo.onboarding.PhoneDto;
import com.vivvo.onboarding.entity.Phone;
import com.vivvo.onboarding.exception.NotFoundException;
import com.vivvo.onboarding.exception.ValidationException;
import com.vivvo.onboarding.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PhoneService {
    private static final String AUTH_TOKEN = "15811c180ff7f9d241ef28744f238e48";
    private static final String ACCOUNT_SID = "ACf6b24a08ebbd871fe07fb219cb03844d";

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhoneAssembler phoneAssembler;

    @Autowired
    private PhoneValidator phoneValidator;

    public PhoneDto create(PhoneDto dto) {
        Map<String, String> errors = phoneValidator.validate(dto);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        dto.setVerified(false);

        PhoneDto newPhone = Optional.of(dto)
                .map(phoneAssembler::disassemble)
                .map(phoneRepository::save)
                .map(phoneAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);

        return newPhone;
    }


    public PhoneDto update(PhoneDto dto) {
        Map<String, String> errors = phoneValidator.validateForUpdate(dto);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return Optional.of(dto)
                .map(phoneAssembler::disassemble)
                .map(phoneRepository::save)
                .map(phoneAssembler::assemble)
                .orElseThrow(IllegalArgumentException::new);
    }

    public PhoneDto get(UUID phoneId) {
        return phoneRepository.findById(phoneId)
                .map(phoneAssembler::assemble)
                .orElseThrow(() -> new NotFoundException(phoneId));
    }


    public List<PhoneDto> getByUserId(UUID userId) {
        return phoneRepository.findByUserId(userId)
                .stream()
                .map(phoneAssembler::assemble)
                .collect(Collectors.toList());
    }


    public void delete(UUID phoneId) {
        Optional<Phone> phone = phoneRepository.findById(phoneId);
        if (phone.isPresent()) {
            phoneRepository.delete(phone.get());
        } else {
            throw new NotFoundException(phoneId);
        }
    }

    public PhoneDto makePrimary(UUID phoneId) {

        List<PhoneDto> allUserPhones = getByUserId(get(phoneId).getUserId());
        for (PhoneDto userPhone : allUserPhones) {
            update(userPhone.setPrimary(false));
        }

        return update(get(phoneId)
                .setPrimary(true));
    }

    public void startTwilioVerify(UUID phoneID){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        PhoneDto dto = get(phoneID);

        UUID verificationCode = UUID.randomUUID();

        update(get(dto.getPhoneId()).setVerificationCode(verificationCode));

        Message message = Message.creator(new PhoneNumber(dto.getPhoneNumber()),
                new PhoneNumber("+13069943159"),
                "Click this to verify your phone number: " + "http://localhost:4444/api/v1/users/" + dto.getUserId() + "/phones/" + dto.getPhoneId() + "/verify/" + dto.getVerificationCode())
                .create();


        ListenableFuture<ResourceSet<Message>> future = Message.reader().readAsync();
        Futures.addCallback(
                future,
                new FutureCallback<ResourceSet<Message>>() {
                    public void onSuccess(ResourceSet<Message> messages) {
                        for (Message message : messages) {
                            System.out.println(message.getSid() + " : " + message.getStatus());
                        }
                    }
                    public void onFailure(Throwable t) {
                        System.out.println("Failed to get message status: " + t.getMessage());
                    }
                });

    }

    public PhoneDto verifyPhoneNumber(UUID phoneId, UUID verificationCode){
        PhoneDto phone = get(phoneId);

        if(phone.getVerified()){
            return phone;
        }

        if(phone.getVerificationCode() != null && phone.getVerificationCode().equals(verificationCode)){
            return update(phone.setVerified(true));
        }else{
            throw new BadRequestException();
        }
    }

}
