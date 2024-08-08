package br.com.fiap.fastfood.api.core.domain.port.outbound;

public interface EmailSenderPort {

  void send(String message, String email, String subject);

}
