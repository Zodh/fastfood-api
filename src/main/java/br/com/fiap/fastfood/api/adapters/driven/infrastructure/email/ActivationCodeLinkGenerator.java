package br.com.fiap.fastfood.api.adapters.driven.infrastructure.email;

import br.com.fiap.fastfood.api.core.application.dto.customer.activation.ActivationCodeDTO;
import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.application.port.outbound.ActivationCodeLinkGeneratorPort;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActivationCodeLinkGenerator implements ActivationCodeLinkGeneratorPort {

  @Value("${server.port}")
  private int port;

  @Override
  public String generate(ActivationCodeDTO activationCode) {
    try {
      return InetAddress.getLocalHost().getHostAddress() + ":" + port + "/activation-code/" + activationCode.getKey().toString();
    } catch (UnknownHostException e) {
      throw new ApplicationException("Falha ao gerar o link de confirmação!", e.getMessage());
    }
  }
}
