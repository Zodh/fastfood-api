package br.com.fiap.fastfood.api.core.domain.model.followup.state;

import br.com.fiap.fastfood.api.core.domain.model.followup.FollowUp;

public abstract class FollowUpState {

  protected FollowUp followUp;

  public FollowUpState(FollowUp followUp) {
    this.followUp = followUp;
  }

  public abstract void setReceived();
  public abstract void setInPreparation();
  public abstract void setReady();
  public abstract void setFinished();

}
