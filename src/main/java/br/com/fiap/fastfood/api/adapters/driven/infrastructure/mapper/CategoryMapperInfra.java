package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.category.CategoryEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.core.application.dto.category.CategoryDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CategoryMapperInfra {

  @Mapping(source = "entity.categoryProducts", target = "products", qualifiedByName = "mapListToDTO")
  CategoryDTO toDTO(CategoryEntity entity);

  @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapListToDTO")
  MenuProductDTO toDTO(MenuProductEntity entity);

  @Named("mapListToDTO")
  default List<MenuProductDTO> mapListToDTO(List<MenuProductEntity> categoryProducts) {
    if (CollectionUtils.isEmpty(categoryProducts)) {
      return new ArrayList<>();
    }
    return categoryProducts.stream().map(this::toDTO).collect(Collectors.toList());
  }

  @Mapping(source = "dto.products", target = "categoryProducts", qualifiedByName = "mapListToEntity")
  CategoryEntity toEntity(CategoryDTO dto);

  @Mapping(source = "dto.ingredients", target = "ingredients", qualifiedByName = "mapListToEntity")
  MenuProductEntity toEntity(MenuProductDTO dto);

  @Named("mapListToEntity")
  default List<MenuProductEntity> mapListToEntity(List<MenuProductDTO> menuProducts) {
    if (CollectionUtils.isEmpty(menuProducts)) {
      return new ArrayList<>();
    }
    return menuProducts.stream().map(this::toEntity).collect(Collectors.toList());
  }

}
