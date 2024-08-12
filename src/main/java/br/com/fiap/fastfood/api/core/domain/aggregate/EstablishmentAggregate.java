package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.port.outbound.EmailSenderPort;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class EstablishmentAggregate {

  private Order root;
  private EmailSenderPort emailSenderPort;

  public EstablishmentAggregate(Order root) {
    checkOrder(root);
    this.root = root;
  }

  public EstablishmentAggregate(Order root, EmailSenderPort emailSenderPort) {
    checkOrder(root);
    this.root = root;
    this.emailSenderPort = emailSenderPort;
  }

  private static void checkOrder(Order root) {
    if (Objects.isNull(root)) {
      throw new DomainException(new ErrorDetail("order", "O pedido não pode ser nulo!"));
    }
  }

  public void turnReadyToPrepare() {
    root.getState().setAwaitingPreparation();
  }

  public void initializePreparation() {
    root.getState().initializePreparation();
  }

  public void setReadyToCollection() {
    root.getState().setReadyToCollection();
    if (
        Objects.nonNull(root.getCustomer())
            && Objects.nonNull(root.getCustomer().getEmail())
            && StringUtils.isNotBlank(root.getCustomer().getEmail().getValue())
            && StringUtils.isNotBlank(root.getCustomer().getFirstName())
    ) {
      final String subject = "Seu pedido está pronto!";
      final String message = String.format("""
          
          Olá, %s!
          
          O seu pedido Nº %d está pronto!
          
          Passe no balcão para retirá-lo!
          
          """, root.getCustomer().getFirstName(), root.getId());
      emailSenderPort.send(message, root.getCustomer().getEmail().getValue(), subject);
    }
  }

  public void finishOrder () {
    root.getState().finish();
  }

}
