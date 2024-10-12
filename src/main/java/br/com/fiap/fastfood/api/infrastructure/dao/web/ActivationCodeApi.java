package br.com.fiap.fastfood.api.infrastructure.dao.web;

import br.com.fiap.fastfood.api.adapters.controller.ActivationCodeController;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeLinkGeneratorGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.EmailSenderGatewayImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/activation-code")
public class ActivationCodeApi {


    private final ActivationCodeController activationCodeController;

    @Autowired
    public ActivationCodeApi(CustomerRepositoryAdapterImpl customerRepositoryAdapter,
                             EmailSenderGatewayImpl emailSenderGatewayImpl,
                             ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
                             ActivationCodeLinkGeneratorGatewayImpl activationCodeLinkGeneratorGatewayImpl) {
        this.activationCodeController = new ActivationCodeController(customerRepositoryAdapter,
                emailSenderGatewayImpl, activationCodeRepositoryAdapter, activationCodeLinkGeneratorGatewayImpl);

    }


    @GetMapping(value = "/{code}")
    public ResponseEntity<Void> activate(@PathVariable UUID code) {
        activationCodeController.activate(code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/generate")
    public ResponseEntity<Void> generate(@RequestParam(value = "email") String email) {
        activationCodeController.generate(email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
