package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpResponseDTO;
import java.util.List;

public interface FollowUpServicePort {

  List<FollowUpResponseDTO> findAll();

}
