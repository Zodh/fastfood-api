package br.com.fiap.fastfood.api.application.gateway;

public interface EmailSenderGateway {

  void send(String message, String email, String subject);

}
