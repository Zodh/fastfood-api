package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.core.application.dto.customer.activation.ActivationCodeDTO;
import java.util.UUID;

public interface ActivationCodeRepositoryGateway extends
    BaseRepositoryGateway<ActivationCodeDTO, UUID> {

  void deleteAllActivationCodeByCustomer(Long customerId);

}
