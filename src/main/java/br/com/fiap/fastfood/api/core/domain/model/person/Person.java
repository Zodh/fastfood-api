package br.com.fiap.fastfood.api.core.domain.model.person;

import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Email;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.PhoneNumber;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person {

  protected Long id;
  protected String name;
  protected Document document;
  protected Email email;
  protected LocalDate birthdate;
  protected PhoneNumber phoneNumber;
  protected boolean active;

}
