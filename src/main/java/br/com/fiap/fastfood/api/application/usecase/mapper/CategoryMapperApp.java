package br.com.fiap.fastfood.api.application.usecase.mapper;

import br.com.fiap.fastfood.api.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.domain.entity.category.Category;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CategoryMapperApp {

  Category toDomain(CategoryDTO category);

  List<CategoryDTO> toCategoryDTO(List<Category> categories);

  CategoryDTO toCategoryDTO(Category category);

}
