package br.com.fiap.fastfood.api.application.gateway;

import br.com.fiap.fastfood.api.application.dto.customer.activation.ActivationCodeDTO;

public interface ActivationCodeLinkGeneratorGateway {

  String generate(ActivationCodeDTO activationCode);

}
