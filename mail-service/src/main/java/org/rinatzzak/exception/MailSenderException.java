package org.rinatzzak.exception;

public class MailSenderException extends Throwable {
    private String message;

    public MailSenderException(String message) {
        super(message);
    }
}
