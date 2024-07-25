package br.com.fiap.fastfood.api.core.domain.service;

import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import br.com.fiap.fastfood.api.core.domain.model.person.Person;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.ActivationCodeLinkGeneratorPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.ActivationCodeRepositoryPort;
import java.time.LocalDateTime;

public class ActivationCodeService {

  public static final int EXPIRATION_TIME_IN_MINUTES = 5;
  private final ActivationCodeRepositoryPort activationCodeRepository;
  private final ActivationCodeLinkGeneratorPort linkGenerator;

  public ActivationCodeService(ActivationCodeRepositoryPort activationCodeRepository, ActivationCodeLinkGeneratorPort linkGenerator) {
    this.activationCodeRepository = activationCodeRepository;
    this.linkGenerator = linkGenerator;
  }

  public String generate(Customer customer) {
    ActivationCode activationCode = new ActivationCode(customer, LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MINUTES));
    ActivationCode persistedActivationCode = activationCodeRepository.save(activationCode);
    return linkGenerator.generate(persistedActivationCode);
  }

}
