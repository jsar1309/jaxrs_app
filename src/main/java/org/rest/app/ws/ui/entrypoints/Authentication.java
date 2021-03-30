package org.rest.app.ws.ui.entrypoints;

import org.rest.app.ws.service.AuthenticationService;
import org.rest.app.ws.shared.dto.UserDTO;
import org.rest.app.ws.ui.model.request.LoginCredentials;
import org.rest.app.ws.ui.model.response.AuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/authentication")
public class Authentication {

    @Autowired
    private AuthenticationService authenticationService;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AuthenticationDetails userLogin(LoginCredentials loginCredentials){
        UserDTO authUser = authenticationService.authenticate(loginCredentials.getUserName(), loginCredentials.getUserPassword());
        String accessToken = authenticationService.generateToken(authUser);
        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setId(authUser.getUserId());
        authenticationDetails.setToken(accessToken);
        return authenticationDetails;
    }

}
