package org.rinatzzak.service;

import org.rinatzzak.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
