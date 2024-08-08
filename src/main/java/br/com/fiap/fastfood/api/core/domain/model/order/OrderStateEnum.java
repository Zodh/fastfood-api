package br.com.fiap.fastfood.api.core.domain.model.order;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStateEnum {

  IN_CREATION("O cliente está criando o pedido!"),
  AWAITING_PAYMENT("O cliente revisou o pedido e o sistema está aguardando o cliente realizar o pagamento!"),
  RECEIVED("O cliente pagou o pedido e o estabelecimento iniciará o preparo em breve!"),
  IN_PREPARATION("O estabelecimento está preparando o pedido!"),
  READY("O pedido está pronto para ser retirado!"),
  FINISHED("O pedido foi finalizado!"),
  CANCELLED("O pedido foi cancelado!");

  private final String description;

}
