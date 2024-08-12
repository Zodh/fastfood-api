package br.com.fiap.fastfood.api.core.domain.model.followup.state;

import lombok.Getter;

@Getter
public enum FollowUpStateEnum {
  RECEIVED("O pedido foi recebido!"),
  IN_PREPARATION("O pedido está sendo preparado!"),
  READY("O pedido está pronto para ser retirado!"),
  FINISHED("O pedido foi finalizado!");

  private final String description;

  FollowUpStateEnum(String description) {
    this.description = description;
  }
}
