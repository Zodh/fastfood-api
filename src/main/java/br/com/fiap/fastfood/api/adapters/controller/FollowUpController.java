package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.FollowUpRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.application.dto.followup.FollowUpResponseDTO;
import br.com.fiap.fastfood.api.application.service.FollowUpService;
import br.com.fiap.fastfood.api.application.service.impl.FollowUpServiceImpl;
import java.util.List;

public class FollowUpController {

  private final FollowUpService followUpService;

  public FollowUpController(FollowUpRepositoryAdapterImpl followUpRepositoryAdapter) {
    this.followUpService = new FollowUpServiceImpl(followUpRepositoryAdapter);
  }

  public List<FollowUpResponseDTO> getFollowUp() {
    return followUpService.findAll();
  }
}
