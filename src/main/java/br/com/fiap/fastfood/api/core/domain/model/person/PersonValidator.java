package br.com.fiap.fastfood.api.core.domain.model.person;

import static java.util.Objects.isNull;

import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

public class PersonValidator implements Validator<Person> {

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
