package org.uce.controller;

import org.uce.entity.User;
import org.uce.dto.RegisterUserRequest;
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
    public Response updateUser(@PathParam("id") Long id, RegisterUserRequest updatedUser) {
        User user = User.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
        // Validar campos obligatorios
        if (updatedUser.email == null || updatedUser.email.isBlank() ||
            updatedUser.username == null || updatedUser.username.isBlank() ||
            updatedUser.password == null || updatedUser.password.isBlank() ||
            updatedUser.fullName == null || updatedUser.fullName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Faltan campos obligatorios: email, username, password, fullName").build();
        }
        // Actualizar todos los campos permitidos
        user.email = updatedUser.email;
        user.username = updatedUser.username;
        user.passwordHash = updatedUser.password; // Usar password del DTO
        user.fullName = updatedUser.fullName;
        user.phoneNumber = updatedUser.phoneNumber;
        user.address = updatedUser.address;
        if (updatedUser.role != null) {
            try {
                user.role = User.Role.valueOf(updatedUser.role);
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Rol inválido").build();
            }
        } else {
            user.role = null;
        }
        user.isActive = true; // Siempre activo al actualizar completamente
        user.persist();
        return Response.ok(user).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response partialUpdateUser(@PathParam("id") Long id, RegisterUserRequest updatedUser) {
        User user = User.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();
        }
        // Solo actualiza los campos que no sean null en el request
        if (updatedUser.email != null && !updatedUser.email.isBlank()) user.email = updatedUser.email;
        if (updatedUser.username != null && !updatedUser.username.isBlank()) user.username = updatedUser.username;
        if (updatedUser.password != null && !updatedUser.password.isBlank()) user.passwordHash = updatedUser.password;
        if (updatedUser.fullName != null && !updatedUser.fullName.isBlank()) user.fullName = updatedUser.fullName;
        if (updatedUser.phoneNumber != null) user.phoneNumber = updatedUser.phoneNumber;
        if (updatedUser.address != null) user.address = updatedUser.address;
        if (updatedUser.role != null) {
            try {
                user.role = User.Role.valueOf(updatedUser.role);
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Rol inválido").build();
            }
        }
        // No se actualiza isActive en PATCH para evitar errores involuntarios
        // Validar que los campos obligatorios no queden nulos
        if (user.email == null || user.email.isBlank() ||
            user.username == null || user.username.isBlank() ||
            user.passwordHash == null || user.passwordHash.isBlank() ||
            user.fullName == null || user.fullName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No puedes dejar campos obligatorios nulos o vacíos: email, username, password, fullName").build();
        }
        user.persist();
        return Response.ok(user).build();
    }
}
