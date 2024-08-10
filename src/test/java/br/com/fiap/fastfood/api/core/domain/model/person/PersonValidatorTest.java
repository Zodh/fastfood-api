package br.com.fiap.fastfood.api.core.domain.model.person;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Email;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.PhoneNumber;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PersonValidatorTest {

  private PersonValidator personValidator = new PersonValidator();

  @Test
  @DisplayName("Given Person With Invalid Min Age "
      + "When Validate "
      + "Then Return Error")
  void invalidMinAgeTest() {
    LocalDate nineYearsOld = LocalDate.now().minusYears(9);
    Person person = mockValidPerson();
    person.setBirthdate(nineYearsOld);
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person.birthdate", "A data de nascimento não é válida! A idade precisa ser entre 10 e 110."));
  }

  @Test
  @DisplayName("Given Person With Invalid Max Age "
      + "When Validate"
      + "Then Return Error")
  void invalidMaxAge() {
    LocalDate nineYearsOld = LocalDate.now().minusYears(111);
    Person person = mockValidPerson();
    person.setBirthdate(nineYearsOld);
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person.birthdate", "A data de nascimento não é válida! A idade precisa ser entre 10 e 110."));
  }

  @Test
  @DisplayName("Given Null Person "
      + "When Validate "
      + "Then Return Error")
  void nullPersonTest() {
    List<ErrorDetail> errors = personValidator.validate(null);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person", "A pessoa não pode ser nula!"));
  }

  @Test
  @DisplayName("Given Person With Invalid CPF "
      + "When Validate "
      + "Then Return Error")
  void invalidDocumentTest() {
    Person person = mockValidPerson();
    person.setDocument(new Document("1982745", "CPF"));
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person.document", "O documento não é válido! Precisa ter o formato XXX.XXX.XXX-XX."));
  }

  @Test
  @DisplayName("Given Person With Invalid Email "
      + "When Validate "
      + "Then Return Error")
  void invalidEmailTest() {
    Person person = mockValidPerson();
    person.setEmail(new Email("test@@test.com"));
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person.email", "O email não é válido! Precisa ter o formato algum.email@teste.com."));
  }

  @Test
  @DisplayName("Given Person With Invalid Phone Number "
      + "When Validate "
      + "Then Return Error")
  void invalidPhoneTest() {
    Person person = mockValidPerson();
    person.setPhoneNumber(new PhoneNumber("1999205"));
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person.phoneNumber", "O telefone não é válido! Precisa ter o formato (XX) XXXXX-XXXX."));
  }

  @Test
  @DisplayName("Given Person With Invalid Name Min Length "
      + "When Validate "
      + "Then Return Error")
  void invalidNameMinLength() {
    Person person = mockValidPerson();
    person.setName("Lu");
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person.name", "O nome não é válido! Precisa conter entre 3 e 150 caracteres!"));
  }

  @Test
  @DisplayName("Given Person With Invalid Name Max Length "
      + "When Validate "
      + "Then Return Error")
  void invalidNameMaxLength() {
    Person person = mockValidPerson();
    person.setName("Lu".repeat(100));
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isNotEmpty().hasSize(1).containsOnly(new ErrorDetail("person.name", "O nome não é válido! Precisa conter entre 3 e 150 caracteres!"));
  }

  @Test
  @DisplayName("Given Valid Person "
      + "When Validate "
      + "Then Return Empty")
  void validPersonTest() {
    Person person = mockValidPerson();
    List<ErrorDetail> errors = personValidator.validate(person);
    assertThat(errors).isEmpty();
  }

  private Person mockValidPerson() {
    Customer validPerson = new Customer();
    validPerson.setDocument(new Document("123.123.123-12","CPF"));
    validPerson.setEmail(new Email("test@test.com"));
    validPerson.setPhoneNumber(new PhoneNumber("(99) 99999-9999"));
    validPerson.setName("John");
    validPerson.setBirthdate(LocalDate.now().minusYears(20));
    return validPerson;
  }

}
