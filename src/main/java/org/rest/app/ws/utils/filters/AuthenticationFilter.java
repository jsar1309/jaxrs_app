package org.rest.app.ws.utils.filters;

import org.rest.app.ws.service.UsersService;
import org.rest.app.ws.shared.dto.UserDTO;
import org.rest.app.ws.utils.UserProfileUtilities;
import org.rest.app.ws.utils.annotations.AUTH;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.security.sasl.AuthenticationException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@AUTH
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Autowired
    UsersService usersService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authorization = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer"))
            throw new AuthenticationException("Authorization header has not been provided");
        String token = authorization.substring("Bearer".length()).trim();
        String userId = containerRequestContext.getUriInfo().getPathParameters().getFirst("user-id");
        validateToken(token, userId);
    }

    private void validateToken(String token, String userId){
        UserDTO userDTO = usersService.getUser(Integer.valueOf(userId));

        String fullToken = new StringBuilder(userDTO.getToken()).append(token).toString();

        String accessTokenMaterial = new StringBuilder(userDTO.getUserId()).append(userDTO.getSalt()).toString();

        try {
            byte[] encryptedAccessToken = UserProfileUtilities.encrypt(userDTO.getEncryptedPassword(), accessTokenMaterial);
            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedAccessToken);
            if (!encryptedBase64.equalsIgnoreCase(fullToken))
                throw new AuthenticationException("Invalid provided token");
        } catch (InvalidKeySpecException ie){
            throw new org.rest.app.ws.exceptions.AuthenticationException("Failed to issue secure access token");
        } catch (AuthenticationException ae){

        }

    }

}
