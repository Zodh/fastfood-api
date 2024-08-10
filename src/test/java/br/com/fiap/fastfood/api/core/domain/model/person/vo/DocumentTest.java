package br.com.fiap.fastfood.api.core.domain.model.person.vo;


import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.DocumentType;
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
