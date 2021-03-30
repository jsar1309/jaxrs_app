package org.rest.app.ws.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rest.app.ws.exceptions.MissingRequiredFieldException;
import org.rest.app.ws.io.dao.DAO;
import org.rest.app.ws.service.UsersService;
import org.rest.app.ws.shared.dto.UserDTO;
import org.rest.app.ws.utils.UserProfileUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersServiceImpl implements UsersService {

    private static Log LOG = LogFactory.getLog(UsersServiceImpl.class);

    @Autowired
    DAO dao;

    UserProfileUtilities utilities = new UserProfileUtilities();

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserDTO dto = new UserDTO();
        utilities.validateRequiredFields(userDTO);

        if (dao.getUserByUserName(userDTO.getEmail()) != null)
            throw new MissingRequiredFieldException("usuario ya existe");

        //Generate secure ID
        userDTO.setUserId(UserProfileUtilities.generateUUID());
        String salt = utilities.generateSalt(30);
        userDTO.setEncryptedPassword(UserProfileUtilities.generateSecurePassword(userDTO.getPassword(), salt));
        userDTO.setSalt(salt);
        try {
            return dao.registerInDB(userDTO);
        } catch (Exception e){
            LOG.info(e);
            return null;
        }
    }

    @Override
    public UserDTO getUser(Integer id) {
        try {
            return dao.getUserFromDB(id);
        } catch (Exception e){
            LOG.info(e);
            return null;
        }
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        try {
            return dao.getUserByUserName(userName);
        } catch (Exception e){
            LOG.info(e);
            return null;
        }
    }


}
