package br.com.fiap.fastfood.api.core.domain.ports.outbound;

public interface EmailSenderPort {

  void send(String message, String email, String subject);

}
