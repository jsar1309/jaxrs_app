package org.rest.app.ws.io.dao;

import org.rest.app.ws.shared.dto.UserDTO;

public interface DAO {

    UserDTO getUserByUserName(String userName);

    UserDTO registerInDB(UserDTO userDTO) throws Exception;

    UserDTO getUserFromDB(Integer id) throws Exception;

    UserDTO updateUser(UserDTO userDTO);

}
