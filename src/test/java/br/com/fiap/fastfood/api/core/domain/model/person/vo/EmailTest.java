package br.com.fiap.fastfood.api.core.domain.model.person.vo;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.fastfood.api.core.domain.model.person.vo.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  @DisplayName("Given Valid Email "
      + "When Verify if it is valid "
      + "Then Return True")
  void isValidEmail() {
    Email email = new Email("felipe.carvalho.bs.contato@gmail.com");
    assertThat(email.isValid()).isTrue();
  }

  @Test
  @DisplayName("Given Invalid Email "
      + "When Verify if it is valid "
      + "Then Return False")
  void isNotValidEmail() {
    Email email = new Email("felipe.carvalho.bs.contato@@gmail.com");
    assertThat(email.isValid()).isFalse();
  }

}
