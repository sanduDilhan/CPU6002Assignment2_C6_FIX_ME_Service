package com.sentura.bLow.controller;

import com.sentura.bLow.dto.NewsLetterSubscriptionRequestDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.MailchimpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mailchimp")
public class MailchimpSubscriptionController {

    @Autowired
    private MailchimpService mailchimpService;

    @PostMapping("/subscription")
    public ResponseEntity<ResponseDto> subscribe(@RequestBody NewsLetterSubscriptionRequestDTO subscriptionRequestDTO) {
        return new ResponseEntity<>(mailchimpService.subscribeService(subscriptionRequestDTO), HttpStatus.OK);
    }
}
