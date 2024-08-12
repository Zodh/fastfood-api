package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderProductMapperInfra {

  @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapListToDTO")
  @Mapping(source = "entity.optionals", target = "optionals", qualifiedByName = "mapListToDTO")
  @Mapping(source = "entity.menuProduct", target = "menuProduct", qualifiedByName = "mapMenuProductToDTO")
  OrderProductDTO toDTO(OrderProductEntity entity);

  @Named("mapListToDTO")
  default List<OrderProductDTO> mapListToDTO(List<OrderProductEntity> orderProductEntities) {
    return Optional.ofNullable(orderProductEntities).orElse(new ArrayList<>()).stream().map(this::toDTO).collect(
        Collectors.toList());
  }

  @Mapping(source = "dto.ingredients", target = "ingredients", qualifiedByName = "mapListToEntity")
  @Mapping(source = "dto.optionals", target = "optionals", qualifiedByName = "mapListToEntity")
  @Mapping(source = "dto.menuProduct", target = "menuProduct", qualifiedByName = "mapMenuProductToEntity")
  OrderProductEntity toEntity(OrderProductDTO dto);

  @Named("mapListToEntity")
  default List<OrderProductEntity> mapListToEntity(List<OrderProductDTO> orderProducts) {
    return Optional.ofNullable(orderProducts).orElse(new ArrayList<>()).stream().map(this::toEntity).collect(
        Collectors.toList());
  }

  @Named("mapMenuProductToDTO")
  default MenuProductDTO mapMenuProductToDTO(MenuProductEntity menuProductEntity) {
    MenuProductMapperInfra menuProductMapperInfra = new MenuProductMapperInfraImpl();
    return menuProductMapperInfra.toDTO(menuProductEntity);
  }

  @Named("mapMenuProductToEntity")
  default MenuProductEntity mapMenuProductToEntity(MenuProductDTO menuProduct) {
    MenuProductMapperInfra menuProductMapperInfra = new MenuProductMapperInfraImpl();
    return menuProductMapperInfra.toEntity(menuProduct);
  }

}
