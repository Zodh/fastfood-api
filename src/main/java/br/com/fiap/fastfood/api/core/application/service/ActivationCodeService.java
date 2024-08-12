package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.activation.ActivationCodeDTO;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.mapper.ActivationCodeMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.ActivationCodeMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.outbound.ActivationCodeLinkGeneratorPort;
import br.com.fiap.fastfood.api.core.application.port.repository.ActivationCodeRepositoryPort;
import br.com.fiap.fastfood.api.core.application.port.repository.CustomerRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
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
  private final CustomerMapperApp customerMapperApp;
  private final ActivationCodeMapperApp activationCodeMapperApp;

  public ActivationCodeService(ActivationCodeRepositoryPort activationCodeRepository,
      ActivationCodeLinkGeneratorPort linkGenerator, CustomerRepositoryPort customerRepository) {
    this.activationCodeRepository = activationCodeRepository;
    this.linkGenerator = linkGenerator;
    this.customerRepository = customerRepository;
    this.customerMapperApp = new CustomerMapperAppImpl();
    this.activationCodeMapperApp = new ActivationCodeMapperAppImpl();
  }

  public String generate(CustomerDTO customerDTO) {
    Customer customer = customerMapperApp.toDomain(customerDTO);
    ActivationCode activationCode = new ActivationCode(customer,
        LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MINUTES));
    if (!activationCode.isValid()) {
      ErrorDetail errorDetail = new ErrorDetail("activationCode.person",
          "A pessoa não pode ser nula e deve estar inativa!");
      throw new DomainException(errorDetail);
    }
    ActivationCodeDTO validActivationCode = activationCodeMapperApp.toDTO(activationCode);
    ActivationCodeDTO persistedActivationCode = activationCodeRepository.save(validActivationCode);
    return linkGenerator.generate(persistedActivationCode);
  }

  public void activate(UUID code) {
    if (Objects.isNull(code)) {
      ErrorDetail errorDetail = new ErrorDetail(ACTIVATION_CODE,
          "O código de ativação é inválido!");
      throw new DomainException(errorDetail);
    }
    Optional<ActivationCodeDTO> activationCodeOpt = activationCodeRepository.findById(code);
    if (activationCodeOpt.isEmpty()) {
      throw new NotFoundException("O código de ativação não foi encontrado!");
    }
    ActivationCodeDTO activationCodeDTO = activationCodeOpt.get();
    ActivationCode activationCode = activationCodeMapperApp.toDomain(activationCodeDTO);
    if (!activationCode.isValid()) {
      ErrorDetail errorDetail = new ErrorDetail(ACTIVATION_CODE,
          "O código de ativação não é mais válido!");
      throw new DomainException(errorDetail);
    }
    customerRepository.activate(activationCodeDTO.getCustomer().getId());
    activationCodeRepository.deleteAllActivationCodeByCustomer(
        activationCodeDTO.getCustomer().getId());
  }

}
