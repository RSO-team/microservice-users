package si.fri.rsoteam.api.v1.resources;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import com.kumuluz.ee.metrics.producers.MetricRegistryProducer;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
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

@ApplicationScoped
@Path("/users")
@Log
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {

    private static final Logger LOG = LogManager.getLogger(UsersResource.class.getName());

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
    @Log(LogParams.METRICS)
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
                    content = @Content(schema = @Schema(implementation = UserDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )
    })
    @Log(LogParams.METRICS)
    public Response getUsers() {
        LOG.warn("Hey, somebody requested all of the user data!");
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
    @Log(LogParams.METRICS)
    @Metered(name = "create_user_metered")
    @Counted(name = "create_user_counted")
    public Response createUser(UserDto userDto) {
        return Response.ok(usersBean.createUser(userDto)).build();
    }

    @PUT
    @Operation(summary = "Updates user and returns it", description = "Returns user details.")
    @APIResponses({
            @APIResponse(
                    description = "User details",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            )
    })
    @Log(LogParams.METRICS)
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
    @Log(LogParams.METRICS)
    @Path("{id}")
    @Counted(name = "deleted_users_count")
    public Response deleteUser(@PathParam("id") Integer id) {
        try {
            usersBean.deleteUser(id);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return Response.noContent().build();
    }
}