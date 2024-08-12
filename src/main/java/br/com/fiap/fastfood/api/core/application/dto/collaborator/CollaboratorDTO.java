package br.com.fiap.fastfood.api.core.application.dto.collaborator;

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
  private String role;

}
