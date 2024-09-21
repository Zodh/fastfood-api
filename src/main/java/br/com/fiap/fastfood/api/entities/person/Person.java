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
public abstract class Person implements Validator<Person> {

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

  @Override
  public List<ErrorDetail> validate(Person person) {
    List<ErrorDetail> errors = new ArrayList<>();
    if (isNull(person)) {
      errors.add(new ErrorDetail("person", "A pessoa não pode ser nula!"));
      return errors;
    }
    if (isNull(person.getDocument()) || !person.getDocument().isValid()) {
      errors.add(new ErrorDetail("person.document", "O documento não é válido! Precisa ter o formato XXX.XXX.XXX-XX."));
    }
    if (isNull(person.getEmail()) || !person.getEmail().isValid()) {
      errors.add(new ErrorDetail("person.email", "O email não é válido! Precisa ter o formato algum.email@teste.com."));
    }
    if (isNull(person.getPhoneNumber()) || !person.getPhoneNumber().isValid()) {
      errors.add(new ErrorDetail("person.phoneNumber", "O telefone não é válido! Precisa ter o formato (XX) XXXXX-XXXX."));
    }
    if (isNull(person.getName()) || (person.getName().length() < 3 || person.getName().length() > 150)) {
      errors.add(new ErrorDetail("person.name", "O nome não é válido! Precisa conter entre 3 e 150 caracteres!"));
    }
    LocalDate maxAge = LocalDate.now().minusYears(110);
    LocalDate minAge = LocalDate.now().minusYears(10);
    if (isNull(person.getBirthdate()) || (person.getBirthdate().isBefore(maxAge) || person.getBirthdate().isAfter(minAge))) {
      errors.add(new ErrorDetail("person.birthdate", "A data de nascimento não é válida! A idade precisa ser entre 10 e 110."));
    }
    return errors;
  }

}
