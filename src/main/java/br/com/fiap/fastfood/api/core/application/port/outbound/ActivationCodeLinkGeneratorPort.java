package br.com.fiap.fastfood.api.core.application.port.outbound;

import br.com.fiap.fastfood.api.core.application.dto.customer.activation.ActivationCodeDTO;

public interface ActivationCodeLinkGeneratorPort {

  String generate(ActivationCodeDTO activationCode);

}
