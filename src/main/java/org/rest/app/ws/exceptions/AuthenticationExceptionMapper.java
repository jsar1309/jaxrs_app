package org.rest.app.ws.exceptions;

import org.rest.app.ws.ui.model.response.ErrorMessage;
import org.rest.app.ws.ui.model.response.ErrorMessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

    @Override
    public Response toResponse(AuthenticationException e) {
        ErrorMessage errorMessage = new ErrorMessage(
            e.getMessage(), ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage(), ""
        );
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
    }
}
