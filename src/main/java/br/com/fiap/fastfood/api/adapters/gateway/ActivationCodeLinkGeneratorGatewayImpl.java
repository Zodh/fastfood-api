package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.core.application.dto.customer.activation.ActivationCodeDTO;
import br.com.fiap.fastfood.api.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.application.gateway.ActivationCodeLinkGeneratorGateway;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActivationCodeLinkGeneratorGatewayImpl implements ActivationCodeLinkGeneratorGateway {

  @Value("${api.gateway}")
  private String url;

  @Override
  public String generate(ActivationCodeDTO activationCode) {
      return url + "/activation-code/" + activationCode.getKey().toString();
  }
}
