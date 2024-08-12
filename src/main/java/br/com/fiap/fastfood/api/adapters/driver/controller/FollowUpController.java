package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter.FollowUpRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpResponseDTO;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.FollowUpServicePort;
import br.com.fiap.fastfood.api.core.application.service.FollowUpServicePortImpl;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow-up")
public class FollowUpController {

  private final FollowUpServicePort followUpServicePort;

  public FollowUpController(FollowUpRepositoryAdapterImpl followUpRepositoryAdapter) {
    this.followUpServicePort = new FollowUpServicePortImpl(followUpRepositoryAdapter);
  }

  @GetMapping
  public ResponseEntity<List<FollowUpResponseDTO>> getFollowUp() {
    return ResponseEntity.ok(followUpServicePort.findAll());
  }
}
