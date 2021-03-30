package org.rest.app.ws.service;

import org.rest.app.ws.shared.dto.UserDTO;

public interface UsersService {

    UserDTO createUser (UserDTO userDTO);

    UserDTO getUser(Integer id);

    UserDTO getUserByUserName(String userName);

}
