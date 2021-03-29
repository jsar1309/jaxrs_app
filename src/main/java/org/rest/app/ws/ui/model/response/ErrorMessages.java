package org.rest.app.ws.ui.model.response;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("check required fields"),
    RECORD_ALREADY_EXISTS("record already exists"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    AUTHENTICATION_FAILED("Authentication has failed");

    private String errorMessage;

    ErrorMessages(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
