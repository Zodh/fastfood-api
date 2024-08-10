package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.Objects;

public class EstablishmentAggregate {

  private Order root;

  public EstablishmentAggregate(Order root) {
    if (Objects.isNull(root)) {
      throw new DomainException(new ErrorDetail("order", "O pedido não pode ser nulo!"));
    }
    this.root = root;
  }

  public void turnReadyToPrepare() {
    root.getState().setAwaitingPreparation();
  }

  public void initializePreparation() {
    root.getState().initializePreparation();
  }

  public void setReadyToCollection() {
    root.getState().setReadyToCollection();
  }

  public void finishOrder () {
    root.getState().finish();
  }

}
