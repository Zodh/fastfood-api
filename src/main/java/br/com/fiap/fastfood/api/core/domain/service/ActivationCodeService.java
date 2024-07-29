package br.com.fiap.fastfood.api.core.domain.service;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.ActivationCodeLinkGeneratorPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.ActivationCodeRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CustomerRepositoryPort;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ActivationCodeService {

  public static final String ACTIVATION_CODE = "activationCode";
  public static final int EXPIRATION_TIME_IN_MINUTES = 5;
  private final ActivationCodeRepositoryPort activationCodeRepository;
  private final ActivationCodeLinkGeneratorPort linkGenerator;
  private final CustomerRepositoryPort customerRepository;

  public ActivationCodeService(ActivationCodeRepositoryPort activationCodeRepository, ActivationCodeLinkGeneratorPort linkGenerator, CustomerRepositoryPort customerRepository) {
    this.activationCodeRepository = activationCodeRepository;
    this.linkGenerator = linkGenerator;
    this.customerRepository = customerRepository;
  }

  public String generate(Customer customer) {
    ActivationCode activationCode = new ActivationCode(customer, LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MINUTES));
    if (!activationCode.isValid()) {
      ErrorDetail errorDetail = new ErrorDetail("activationCode.person", "A pessoa não pode ser nula e deve estar inativa!");
      throw new DomainException(errorDetail);
    }
    ActivationCode persistedActivationCode = activationCodeRepository.save(activationCode);
    return linkGenerator.generate(persistedActivationCode);
  }

  public void activate(UUID code) {
    if (Objects.isNull(code)) {
      ErrorDetail errorDetail = new ErrorDetail(ACTIVATION_CODE, "O código de ativação é inválido!");
      throw new DomainException(errorDetail);
    }
    Optional<ActivationCode> activationCodeOpt = activationCodeRepository.findById(code);
    if (activationCodeOpt.isEmpty()) {
      throw new NotFoundException("O código de ativação não foi encontrado!");
    }
    ActivationCode activationCode = activationCodeOpt.get();
    if (!activationCode.isValid()) {
      ErrorDetail errorDetail = new ErrorDetail(ACTIVATION_CODE, "O código de ativação não é mais válido!");
      throw new DomainException(errorDetail);
    }
    customerRepository.activate(activationCode.getCustomer().getId());
    activationCodeRepository.deleteAllActivationCodeByCustomer(activationCode.getCustomer().getId());
  }

}
