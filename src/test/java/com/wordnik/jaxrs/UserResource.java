/**
 * Copyright 2014 Reverb Technologies, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wordnik.jaxrs;

import com.wordnik.sample.data.UserData;
import com.wordnik.sample.exception.ApiException;
import com.wordnik.sample.exception.NotFoundException;
import com.wordnik.sample.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@SwaggerDefinition(
        host = "www.example.com:8080",
        basePath = "/api",
        info = @Info(
                title = "Swagger Maven Plugin Sample",
                version = "v1",
                description = "This is a sample.",
                termsOfService = "http://www.github.com/kongchen/swagger-maven-plugin",
                contact = @Contact(name = "Kong Chen", email = "kongchen@gmail.com", url = "http://kongch.com"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        tags = { @Tag(name = "user", description = "Operations about user"),
                 @Tag(name = "spurioustag", description = "Operations about something spurious")
        }
)
@Path("/user")
@Api(value = "/user")
@Produces({"application/json", "application/xml"})
public class UserResource {
    static UserData userData = new UserData();

    @POST
    @ApiOperation(value = "Create user",
            notes = "This can only be done by the logged in user.",
            position = 1)
    public Response createUser(
            @ApiParam(value = "Created user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createWithArray")
    @ApiOperation(value = "Creates list of users with given input array",
            position = 2)
    public Response createUsersWithArrayInput(@ApiParam(value = "List of user object", required = true) User[] users) {
        for (User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createWithList")
    @ApiOperation(value = "Creates list of users with given input array",
            position = 3)
    public Response createUsersWithListInput(@ApiParam(value = "List of user object", required = true) java.util.List<User> users) {
        for (User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

    @PUT
    @Path("/{username}")
    @ApiOperation(value = "Updated user",
            notes = "This can only be done by the logged in user.",
            position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid user supplied"),
            @ApiResponse(code = 404, message = "User not found")})
    public Response updateUser(
            @ApiParam(value = "name that need to be deleted", required = true) @PathParam("username") String username,
            @ApiParam(value = "Updated user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }

    @DELETE
    @Path("/{username}")
    @ApiOperation(value = "Delete user",
            notes = "This can only be done by the logged in user.",
            position = 5)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid username supplied"),
            @ApiResponse(code = 404, message = "User not found")})
    public Response deleteUser(
            @ApiParam(value = "The name that needs to be deleted", required = true) @PathParam("username") String username) {
        userData.removeUser(username);
        return Response.ok().entity("").build();
    }

    @GET
    @Path("/{username}")
    @ApiOperation(value = "Get user by user name",
            response = User.class,
            position = 0)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid username supplied"),
            @ApiResponse(code = 404, message = "User not found")})
    public Response getUserByName(
            @ApiParam(value = "The name that needs to be fetched. Use user1 for testing. ", required = true) @PathParam("username") String username)
            throws ApiException {
        User user = userData.findUserByName(username);
        if (user != null) {
            return Response.ok().entity(user).build();
        } else {
            throw new NotFoundException(404, "User not found");
        }
    }

    @GET
    @Path("/login")
    @ApiOperation(value = "Logs user into the system",
            response = String.class,
            position = 6)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid username/password supplied")})
    public Response loginUser(
            @ApiParam(value = "The user name for login", required = true) @QueryParam("username") String username,
            @ApiParam(value = "The password for login in clear text", required = true) @QueryParam("password") String password) {
        return Response.ok()
                .entity("logged in user session:" + System.currentTimeMillis())
                .build();
    }

    @GET
    @Path("/logout")
    @ApiOperation(value = "Logs out current logged in user session",
            position = 7)
    public Response logoutUser() {
        return Response.ok().entity("").build();
    }
}
