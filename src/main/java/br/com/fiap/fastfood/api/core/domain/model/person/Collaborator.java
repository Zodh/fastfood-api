package br.com.fiap.fastfood.api.core.domain.model.person;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Collaborator extends Person {

  private CollaboratorRoleEnum role;

}