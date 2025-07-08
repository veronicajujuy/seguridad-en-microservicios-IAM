package com.dh.usersservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.dh.usersservice.model.User;
import com.dh.usersservice.model.UserRequest;

@Repository
public class KeyCloakRepository implements IUserRepository{
  @Autowired
  private Keycloak keycloak;
  @Value("${dh.keycloak.realm}")
  private String realm;

  @Override
  public Optional<User> findById(String id) {
    UserRepresentation userRepresentation = keycloak
        .realm(realm)
        .users()
        .get(id)
        .toRepresentation();
    return Optional.of(fromRepresentation(userRepresentation));
  }
  
  @Override
  public List<User> findByUsername(String username) {
    List<UserRepresentation> userRepresentation = keycloak
        .realm(realm)
        .users()
        .search(username);
  
    return userRepresentation.stream().map(user -> fromRepresentation(user)).collect(Collectors.toList());
  }

  @Override
  public User createUser(UserRequest userRequest) {
    // Crear representación del usuario
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(userRequest.getUsername());
    userRepresentation.setFirstName(userRequest.getFirstName());
    userRepresentation.setLastName(userRequest.getLastName());
    userRepresentation.setEmail(userRequest.getEmail());
    userRepresentation.setEnabled(userRequest.isEnabled());
    userRepresentation.setEmailVerified(true);

    // Crear el usuario en Keycloak
    Response response = keycloak.realm(realm).users().create(userRepresentation);

    if (response.getStatus() == 201) {
      // Obtener el ID del usuario creado
      String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

      // Configurar la contraseña
      CredentialRepresentation credential = new CredentialRepresentation();
      credential.setType(CredentialRepresentation.PASSWORD);
      credential.setValue(userRequest.getPassword());
      credential.setTemporary(false);

      keycloak.realm(realm).users().get(userId).resetPassword(credential);

      // Retornar el usuario creado
      return new User(userId, userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail());
    } else {
      throw new RuntimeException("Error al crear el usuario en Keycloak. Status: " + response.getStatus());
    }
  }


  private User fromRepresentation(UserRepresentation userRepresentation) {
    return new User(userRepresentation.getId(),userRepresentation.getFirstName(),userRepresentation.getLastName(),userRepresentation.getEmail());}


}
