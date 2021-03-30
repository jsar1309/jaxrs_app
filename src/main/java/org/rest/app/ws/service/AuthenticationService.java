package org.rest.app.ws.service;

import org.rest.app.ws.exceptions.AuthenticationException;
import org.rest.app.ws.shared.dto.UserDTO;

public interface AuthenticationService {

    UserDTO authenticate(String userName, String password) throws AuthenticationException;

    String generateToken(UserDTO userDTO) throws AuthenticationException;

}
