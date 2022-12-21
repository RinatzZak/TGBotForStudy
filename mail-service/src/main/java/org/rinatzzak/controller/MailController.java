package org.rinatzzak.controller;

import org.rinatzzak.dto.MailParams;
import org.rinatzzak.service.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailSenderService mailSenderService;

    public MailController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @PostMapping
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams) {
        mailSenderService.send(mailParams);
        return ResponseEntity.ok().build();
    }
}