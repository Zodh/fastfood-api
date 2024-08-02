package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderProductMapper {

  @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapListToDomain")
  @Mapping(source = "entity.optionals", target = "optionals", qualifiedByName = "mapListToDomain")
  @Mapping(source = "entity.menuProduct", target = "menuProduct", qualifiedByName = "mapMenuProductToDomain")
  OrderProduct toDomain(OrderProductEntity entity);

  @Named("mapListToDomain")
  default List<OrderProduct> mapListToDomain(List<OrderProductEntity> orderProductEntities) {
    return Optional.ofNullable(orderProductEntities).orElse(Collections.emptyList()).stream().map(this::toDomain).collect(
        Collectors.toList());
  }

  @Mapping(source = "domain.ingredients", target = "ingredients")
  @Mapping(source = "domain.optionals", target = "optionals")
  @Mapping(source = "domain.menuProduct", target = "menuProduct", qualifiedByName = "mapMenuProductToEntity")
  OrderProductEntity toEntity(OrderProduct domain);

  @Named("mapListToEntity")
  default List<OrderProductEntity> mapListToEntity(List<OrderProduct> orderProducts) {
    return Optional.ofNullable(orderProducts).orElse(Collections.emptyList()).stream().map(this::toEntity).collect(
        Collectors.toList());
  }

  @Named("mapMenuProductToDomain")
  default MenuProduct mapMenuProductToDomain(MenuProductEntity menuProductEntity) {
    MenuProductMapper menuProductMapper = new MenuProductMapperImpl();
    return menuProductMapper.toDomain(menuProductEntity);
  }

  @Named("mapMenuProductToEntity")
  default MenuProductEntity mapMenuProductToEntity(MenuProduct menuProduct) {
    MenuProductMapper menuProductMapper = new MenuProductMapperImpl();
    return menuProductMapper.toEntity(menuProduct);
  }

}
