package si.fri.rsoteam.api.v1.resources;

import si.fri.rsoteam.lib.dtos.UserDto;
import si.fri.rsoteam.models.entities.UserEntity;
import si.fri.rsoteam.services.beans.UsersBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    private Logger log = Logger.getLogger(UsersResource.class.getName());

    @Inject
    private UsersBean usersBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("{id}")
    public Response getUserById(@PathParam("id") Integer userId) {
        UserDto user = usersBean.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return Response.ok(user).build();
    }

    @GET
    public Response getUsers() {
        List<UserDto> userList = usersBean.getUsers();
        return Response.ok(userList).build();
    }

    @POST
    public Response createUser(UserDto userDto) {
        return Response.ok(usersBean.createUser(userDto)).build();
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") Integer id, UserDto userDto) {
        return Response.status(201).entity(usersBean.updateUser(userDto, id)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Integer id) {
        usersBean.deleteUser(id);
        return Response.noContent().build();
    }
}