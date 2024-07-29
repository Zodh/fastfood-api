package br.com.fiap.fastfood.api.adapters.driven.infrastructure.email;

import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.ActivationCodeLinkGeneratorPort;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActivationCodeLinkGenerator implements ActivationCodeLinkGeneratorPort {

  @Value("${server.port}")
  private int port;

  @Override
  public String generate(ActivationCode activationCode) {
    try {
      return InetAddress.getLocalHost().getHostAddress() + ":" + port + "/activation-code/" + activationCode.getKey().toString();
    } catch (UnknownHostException e) {
      throw new ApplicationException("Falha ao gerar o link de confirmação!", e.getMessage());
    }
  }
}
