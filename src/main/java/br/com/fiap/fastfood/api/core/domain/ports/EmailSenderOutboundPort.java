package br.com.fiap.fastfood.api.core.domain.ports;

public interface EmailSenderOutboundPort {

  void send(String message, String email, String subject);

}
