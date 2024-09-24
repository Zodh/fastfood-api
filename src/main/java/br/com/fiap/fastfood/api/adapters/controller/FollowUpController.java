package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.FollowUpRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpResponseDTO;
import br.com.fiap.fastfood.api.application.service.FollowUpService;
import br.com.fiap.fastfood.api.application.service.impl.FollowUpServiceImpl;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow-up")
public class FollowUpController {

  private final FollowUpService followUpService;

  public FollowUpController(FollowUpRepositoryAdapterImpl followUpRepositoryAdapter) {
    this.followUpService = new FollowUpServiceImpl(followUpRepositoryAdapter);
  }

  @GetMapping
  public ResponseEntity<List<FollowUpResponseDTO>> getFollowUp() {
    return ResponseEntity.ok(followUpService.findAll());
  }
}
