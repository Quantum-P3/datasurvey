package org.datasurvey.web.rest.errors;

public class EmailNotExistException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailNotExistException() {
        super(ErrorConstants.EMAIL_NOT_EXISTS_TYPE, "Email not exists!", "userManagement", "emailnotexists");
    }
}
