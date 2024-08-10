package br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto.customer;

import br.com.fiap.fastfood.api.core.domain.model.person.vo.DocumentType;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class CustomerDTO {

  private Long id;
  private String name;
  private String email;
  private String phoneNumber;
  private String documentNumber;
  @Builder.Default
  private final DocumentType documentType = DocumentType.CPF;
  private LocalDate birthdate;

}
