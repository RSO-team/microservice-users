package si.fri.rsoteam.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rsoteam.lib.dtos.UserDto;
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
    @Operation(summary = "Get user by given id", description = "Returns user details.")
    @APIResponses({
            @APIResponse(
                    description = "User details",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            )
    })
    @Path("{id}")
    public Response getUserById(@PathParam("id") Integer userId) {
        UserDto user = usersBean.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return Response.ok(user).build();
    }

    @GET
    @Operation(summary = "Get list of users", description = "Returns list of users.")
    @APIResponses({
            @APIResponse(
                    description = "User list",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            )
    })
    public Response getUsers() {
        List<UserDto> userList = usersBean.getUsers();
        return Response.ok(userList).build();
    }

    @POST
    @Operation(summary = "Creates new user and returns it", description = "Returns new user details.")
    @APIResponses({
            @APIResponse(
                    description = "User details",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            )
    })
    public Response createUser(UserDto userDto) {
        return Response.ok(usersBean.createUser(userDto)).build();
    }

    @PUT
    @Operation(summary = "Updates new user and returns it", description = "Returns user details.")
    @APIResponses({
            @APIResponse(
                    description = "User details",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            )
    })
    @Path("{id}")
    public Response updateUser(@PathParam("id") Integer id, UserDto userDto) {
        return Response.status(201).entity(usersBean.updateUser(userDto, id)).build();
    }

    @DELETE
    @Operation(summary = "Deletes specified user", description = "Returns no content.")
    @APIResponses({
            @APIResponse(
                    description = "No content",
                    responseCode = "204"
            )
    })
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Integer id) {
        usersBean.deleteUser(id);
        return Response.noContent().build();
    }
}