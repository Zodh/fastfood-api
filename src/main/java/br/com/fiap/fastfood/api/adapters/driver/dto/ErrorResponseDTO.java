package br.com.fiap.fastfood.api.adapters.driver.dto;

import java.util.List;

public record ErrorResponseDTO(String message, List<ErrorDetailDTO> errors) {

}
