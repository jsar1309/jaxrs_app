package org.rest.app.ws.ui.entrypoints;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rest.app.ws.service.UsersService;
import org.rest.app.ws.service.impl.UsersServiceImpl;
import org.rest.app.ws.shared.dto.UserDTO;
import org.rest.app.ws.ui.model.request.CreateUserRequestModel;
import org.rest.app.ws.ui.model.response.UserProfileRest;
import org.rest.app.ws.utils.annotations.AUTH;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Service
@Path("/users")
public class UserEntryPoint {

    private static Log LOG = LogFactory.getLog(UserEntryPoint.class);

    @Autowired
    UsersService usersService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest createUser(CreateUserRequestModel requestModel){
        LOG.debug(requestModel);
        UserProfileRest userProfileRest = new UserProfileRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(requestModel, userDTO);

        UserDTO dto = usersService.createUser(userDTO);

        BeanUtils.copyProperties(dto, userProfileRest);
        LOG.debug(userProfileRest);
        return userProfileRest;
    }

    @AUTH
    @GET
    @Path("/{user-id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest getUserById(@PathParam("user-id") Integer id){
        UserProfileRest userProfileRest = new UserProfileRest();
        UserDTO dto = usersService.getUser(id);
        BeanUtils.copyProperties(dto, userProfileRest);
        return userProfileRest;
    }

}
