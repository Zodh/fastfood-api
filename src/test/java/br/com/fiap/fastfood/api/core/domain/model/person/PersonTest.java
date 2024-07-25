package br.com.fiap.fastfood.api.core.domain.model.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PersonTest {

  @Test
  @DisplayName("Given Person With Valid Name "
      + "When Get First Name "
      + "Then Split Only String Before First Whitespace")
  void getFirstName() {
    Customer customer = new Customer();
    customer.setName("Felipe Carvalho");
    String result = customer.getFirstName();
    assertThat(result).isEqualTo("Felipe");
  }

  @Test
  @DisplayName("Given Person With Only One Name "
      + "When Get First Name "
      + "Then Return Name")
  void getName() {
    Customer customer = new Customer();
    customer.setName("Felipe");
    String result = customer.getFirstName();
    assertThat(result).isEqualTo("Felipe");
  }

  @Test
  @DisplayName("Given Person With Invalid Name "
      + "When Get First Name "
      + "Then Throws Exception ")
  void exceptionTryingToGetFirstName() {
    Customer customer = new Customer();
    customer.setName("");
    assertThrows(DomainException.class, customer::getFirstName);
  }

}
