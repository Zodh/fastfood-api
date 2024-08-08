package br.com.fiap.fastfood.api.adapters.driven.infrastructure.email;

import br.com.fiap.fastfood.api.core.domain.port.outbound.EmailSenderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender implements EmailSenderPort {

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
