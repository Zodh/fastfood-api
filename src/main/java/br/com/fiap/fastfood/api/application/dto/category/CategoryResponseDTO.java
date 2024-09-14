package br.com.fiap.fastfood.api.application.dto.category;

import java.util.List;

public record CategoryResponseDTO(List<CategoryDTO> categories) {
}