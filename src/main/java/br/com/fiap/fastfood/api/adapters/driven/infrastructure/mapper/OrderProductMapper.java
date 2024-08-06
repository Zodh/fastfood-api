package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.OrderProductDTO;
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

  @Mapping(source = "orderProduct.optionals", target = "optionals", qualifiedByName = "mapDomainToDtoList")
  @Mapping(source = "orderProduct.ingredients", target = "ingredients", qualifiedByName = "mapDomainToDtoList")
  @Mapping(source = "orderProduct.menuProduct", target = "menuProduct", qualifiedByName = "mapDomainToMenuProduct")
  OrderProductDTO toDto(OrderProduct orderProduct);

  @Named("mapDomainToMenuProduct")
  default MenuProductDTO mapDomainToMenuProduct(MenuProduct menuProduct) {
    MenuProductMapper menuProductMapper = new MenuProductMapperImpl();
    return menuProductMapper.toMenuProductDTO(menuProduct);
  }

  @Named("mapDomainToDtoList")
  default List<OrderProductDTO> mapDomainToDtoList(List<OrderProduct> orderProducts) {
    return Optional.ofNullable(orderProducts).orElse(Collections.emptyList()).stream().map(this::toDto).toList();
  }

  @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapListToDomain")
  @Mapping(source = "entity.optionals", target = "optionals", qualifiedByName = "mapListToDomain")
  @Mapping(source = "entity.menuProduct", target = "menuProduct", qualifiedByName = "mapMenuProductToDomain")
  OrderProduct toDomain(OrderProductEntity entity);

  @Mapping(source = "dto.ingredients", target = "ingredients", qualifiedByName = "mapDtoListToDomain")
  @Mapping(source = "dto.optionals", target = "optionals", qualifiedByName = "mapDtoListToDomain")
  @Mapping(source = "dto.menuProduct", target = "menuProduct", qualifiedByName = "mapDtoMenuProductToDomain")
  OrderProduct toDomain(OrderProductDTO dto);

  @Named("mapDtoListToDomain")
  default List<OrderProduct> mapDtoListToDomain(List<OrderProductDTO> dtoList) {
    return Optional.ofNullable(dtoList).orElse(Collections.emptyList()).stream().map(this::toDomain).collect(Collectors.toList());
  }

  @Named("mapDtoMenuProductToDomain")
  default MenuProduct mapDtoMenuProductToDomain(MenuProductDTO menuProductDTO) {
    MenuProductMapper menuProductMapper = new MenuProductMapperImpl();
    return menuProductMapper.toDomain(menuProductDTO);
  }

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
