package br.com.fiap.fastfood.api.core.application.ports.outbound;

import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;

public interface ActivationCodeLinkGeneratorPort {

  String generate(ActivationCode activationCode);

}
