package org.rest.app.ws.service.impl;

import org.rest.app.ws.exceptions.AuthenticationException;
import org.rest.app.ws.io.dao.DAO;
import org.rest.app.ws.service.AuthenticationService;
import org.rest.app.ws.service.UsersService;
import org.rest.app.ws.shared.dto.UserDTO;
import org.rest.app.ws.ui.model.response.ErrorMessages;
import org.rest.app.ws.utils.UserProfileUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private DAO dao;

    @Override
    public UserDTO authenticate(String userName, String password) throws AuthenticationException {
        UserDTO dto = usersService.getUserByUserName(userName);
        if(dto == null)
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        String encryptedPassword = UserProfileUtilities.generateSecurePassword(password, dto.getSalt());
        Boolean auth = false;
        if (encryptedPassword != null && encryptedPassword.equalsIgnoreCase(dto.getEncryptedPassword())) {
            auth = userName != null && userName.equalsIgnoreCase(dto.getEmail());
        }
        if (!auth)
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        return dto;
    }

    @Override
    public String generateToken(UserDTO userDTO) throws AuthenticationException {
        String saltComplement = userDTO.getSalt();
        String accessTokenMaterial = new StringBuilder(userDTO.getUserId()).append(saltComplement).toString();
        try {
            byte[] encryptedAccessToken = UserProfileUtilities.encrypt(userDTO.getEncryptedPassword(), accessTokenMaterial);
            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedAccessToken);
            int tokenLength = encryptedBase64.length();
            String tokenToDB = encryptedBase64.substring(0, tokenLength/2);
            userDTO.setToken(tokenToDB);
            dao.updateUser(userDTO);
            return encryptedBase64.substring(tokenLength/2);
        } catch (InvalidKeySpecException ie){
            throw new AuthenticationException("Failed to issue secure access token");
        }
    }

}
