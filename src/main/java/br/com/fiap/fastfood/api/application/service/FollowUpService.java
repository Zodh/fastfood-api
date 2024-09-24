package br.com.fiap.fastfood.api.application.service;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpResponseDTO;
import java.util.List;

public interface FollowUpService {

  List<FollowUpResponseDTO> findAll();

}
