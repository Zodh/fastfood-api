package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.application.gateway.EmailSenderGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSenderGatewayImpl implements EmailSenderGateway {

  @Value("${spring.application.name}")
  private String from;

  private final JavaMailSender javaMailSender;

  @Override
  public void send(String message, String email, String subject) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(from);
    mailMessage.setTo(email);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);
    javaMailSender.send(mailMessage);
  }

}
