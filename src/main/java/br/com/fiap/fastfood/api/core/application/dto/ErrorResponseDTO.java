package br.com.fiap.fastfood.api.core.application.dto;

import br.com.fiap.fastfood.api.core.application.dto.ErrorDetailDTO;

import java.util.List;

public record ErrorResponseDTO(String message, List<ErrorDetailDTO> errors) {

}
