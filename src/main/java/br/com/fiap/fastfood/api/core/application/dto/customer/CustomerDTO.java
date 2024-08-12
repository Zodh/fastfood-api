package br.com.fiap.fastfood.api.core.application.dto.customer;

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
  private DocumentTypeEnum documentType;
  private LocalDate birthdate;

}
