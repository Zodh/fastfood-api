package br.com.fiap.fastfood.api.entities.followup.state.impl;

import br.com.fiap.fastfood.api.entities.followup.FollowUp;
import br.com.fiap.fastfood.api.entities.followup.state.FollowUpState;

public class OrderReceivedFollowUpState extends FollowUpState {

  public OrderReceivedFollowUpState(
      FollowUp followUp) {
    super(followUp);
  }

  @Override
  public void setReceived() {

  }

  @Override
  public void setInPreparation() {

  }

  @Override
  public void setReady() {

  }

  @Override
  public void setFinished() {

  }
}
