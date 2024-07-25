package br.com.fiap.fastfood.api.core.domain.model.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersonTest {

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

}
