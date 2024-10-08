package br.com.fiap.fastfood.api.entities.person;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Collaborator extends Person {

  private CollaboratorRoleEnum role;

}
