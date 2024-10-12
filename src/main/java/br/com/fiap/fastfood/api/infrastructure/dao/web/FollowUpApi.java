package br.com.fiap.fastfood.api.infrastructure.dao.web;

import br.com.fiap.fastfood.api.adapters.controller.FollowUpController;
import br.com.fiap.fastfood.api.adapters.gateway.FollowUpRepositoryAdapterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/follow-up")
public class FollowUpApi {

    private final FollowUpController followUpController;

    @Autowired
    public FollowUpApi(FollowUpRepositoryAdapterImpl followUpRepositoryAdapter) {
        this.followUpController = new FollowUpController(followUpRepositoryAdapter);
    }

    @GetMapping
    public ResponseEntity<List<br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpResponseDTO>> getFollowUp() {
        return ResponseEntity.ok(followUpController.getFollowUp());
    }
}
