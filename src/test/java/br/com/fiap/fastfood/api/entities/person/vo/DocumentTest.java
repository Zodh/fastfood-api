package br.com.fiap.fastfood.api.entities.person.vo;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DocumentTest {

  @Test
  @DisplayName("Given Valid CPF "
      + "When Call Is Valid "
      + "Then Return True")
  void isValidCpf() {
    Document document = new Document("496.046.288-44", "CPF");
    assertThat(document.isValid()).isTrue();
  }

  @Test
  @DisplayName("Given Invalid CPF "
      + "When Call Is Valid "
      + "Then Return False")
  void isNotValidCpf() {
    Document document = new Document("159283817", "CPF");
    assertThat(document.isValid()).isFalse();
  }

}
