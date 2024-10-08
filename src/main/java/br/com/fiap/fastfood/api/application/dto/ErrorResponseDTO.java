package br.com.fiap.fastfood.api.application.dto;

import java.util.List;

public record ErrorResponseDTO(String message, List<ErrorDetailDTO> errors) {

}
