package org.rest.app.ws.exceptions;

public class AuthenticationException extends RuntimeException{

    private static final long serialVersionUID = -6982659539378636591L;

    public AuthenticationException(String message){
        super(message);
    }
}
