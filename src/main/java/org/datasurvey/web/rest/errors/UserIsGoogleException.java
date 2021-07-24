package org.datasurvey.web.rest.errors;

public class UserIsGoogleException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserIsGoogleException() {
        super(ErrorConstants.USER_IS_GOOGLE_TYOE, "User Is Google", "userManagement", "userisgoogle");
    }
}
