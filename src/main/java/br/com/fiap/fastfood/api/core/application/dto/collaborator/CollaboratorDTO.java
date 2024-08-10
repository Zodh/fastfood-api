package br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto.collaborator;

import br.com.fiap.fastfood.api.core.domain.model.person.CollaboratorRoleEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class CollaboratorDTO {

  private Long id;
  private String name;
  private CollaboratorRoleEnum role;

}
