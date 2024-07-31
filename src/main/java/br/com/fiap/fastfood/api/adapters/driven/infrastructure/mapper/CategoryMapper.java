package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.category.CategoryEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driver.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CategoryMapper {

  @Mapping(source = "entity.categoryProducts", target = "products", qualifiedByName = "mapListToDomain")
  Category toDomain(CategoryEntity entity);

  @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapListToDomain")
  MenuProduct toDomain(MenuProductEntity entity);

  @Named("mapListToDomain")
  default List<MenuProduct> mapListToDomain(List<MenuProductEntity> categoryProducts) {
    if (CollectionUtils.isEmpty(categoryProducts)) {
      return new ArrayList<>();
    }
    return categoryProducts.stream().map(this::toDomain).collect(Collectors.toList());
  }

  Category toDomain(CategoryDTO category);

  List<CategoryDTO> toCategoryDTO(List<Category> categories);

  CategoryDTO toCategoryDTO(Category category);

  @Mapping(source = "domain.products", target = "categoryProducts", qualifiedByName = "mapListToEntity")
  CategoryEntity toEntity(Category domain);

  @Mapping(source = "domain.ingredients", target = "ingredients", qualifiedByName = "mapListToEntity")
  MenuProductEntity toEntity(MenuProduct domain);

  @Named("mapListToEntity")
  default List<MenuProductEntity> mapListToEntity(List<MenuProduct> menuProducts) {
    if (CollectionUtils.isEmpty(menuProducts)) {
      return new ArrayList<>();
    }
    return menuProducts.stream().map(this::toEntity).collect(Collectors.toList());
  }

}
