package org.rest.app.ws.exceptions;

public class MissingRequiredFieldException extends RuntimeException{

    private static final long serialVersionUID = -5585249902435527996L;

    public MissingRequiredFieldException(String message){
        super(message);
    }


}
