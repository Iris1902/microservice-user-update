package org.uce.controller;

import org.uce.entity.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, User updatedUser) {
        User user = User.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
        // Actualizar todos los campos permitidos
        user.email = updatedUser.email;
        user.username = updatedUser.username;
        user.passwordHash = updatedUser.passwordHash;
        user.fullName = updatedUser.fullName;
        user.phoneNumber = updatedUser.phoneNumber;
        user.address = updatedUser.address;
        user.role = updatedUser.role;
        user.isActive = updatedUser.isActive;
        user.persist();
        return Response.ok(user).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response partialUpdateUser(@PathParam("id") Long id, User updatedUser) {
        User user = User.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
        // Solo actualiza los campos que no sean null en el request
        if (updatedUser.email != null) user.email = updatedUser.email;
        if (updatedUser.username != null) user.username = updatedUser.username;
        if (updatedUser.passwordHash != null) user.passwordHash = updatedUser.passwordHash;
        if (updatedUser.fullName != null) user.fullName = updatedUser.fullName;
        if (updatedUser.phoneNumber != null) user.phoneNumber = updatedUser.phoneNumber;
        if (updatedUser.address != null) user.address = updatedUser.address;
        if (updatedUser.role != null) user.role = updatedUser.role;
        // Para boolean, solo actualiza si es diferente
        user.isActive = updatedUser.isActive;
        user.persist();
        return Response.ok(user).build();
    }
}
