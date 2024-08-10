package br.com.fiap.fastfood.api.adapters.driven.infrastructure.dto;

import java.util.List;

public record ErrorResponseDTO(String message, List<ErrorDetailDTO> errors) {

}
