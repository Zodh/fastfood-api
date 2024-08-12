package br.com.fiap.fastfood.api.core.application.dto.category;

import java.util.List;

public record CategoryResponseDTO(List<CategoryDTO> categories) {
}