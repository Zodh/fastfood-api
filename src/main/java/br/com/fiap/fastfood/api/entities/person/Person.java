package br.com.fiap.fastfood.api.entities.person;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.entities.Validator;
import br.com.fiap.fastfood.api.entities.person.vo.Document;
import br.com.fiap.fastfood.api.entities.person.vo.Email;
import br.com.fiap.fastfood.api.entities.person.vo.PhoneNumber;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static java.util.Objects.isNull;

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

  public String getFirstName() {
    if (!StringUtils.hasText(name)) {
      ErrorDetail errorDetail = new ErrorDetail("person.name", "Não foi possível recuperar o primeiro nome! O nome está vazio.");
      throw new DomainException(errorDetail);
    }
    return name.substring(0, StringUtils.containsWhitespace(name) ? name.indexOf(' ') : name.length());
  }

}
