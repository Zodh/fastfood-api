package br.com.fiap.fastfood.api.entities.person.vo;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Email {

  private final String value;

  public boolean isValid() {
    boolean result = true;
    try {
      InternetAddress emailAddr = new InternetAddress(value);
      emailAddr.validate();
    } catch (AddressException ex) {
      result = false;
    }
    return result;
  }

}
