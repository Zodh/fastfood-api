package br.com.fiap.fastfood.api.application.usecase;

import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import java.util.UUID;

public interface CustomerUseCase {

  void register(CustomerDTO customerDTO);
  void activate(UUID code);
  CustomerDTO identify(String documentNumber, DocumentTypeEnum documentType);
  void resendActivationCode(String email);
  CustomerDTO getById(Long id);
  CustomerDTO getByDocument(String documentNumber, DocumentTypeEnum documentType);

}
