package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
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
public interface OrderProductMapperApp {

  @Mapping(source = "orderProduct.optionals", target = "optionals", qualifiedByName = "mapDomainToDtoList")
  @Mapping(source = "orderProduct.ingredients", target = "ingredients", qualifiedByName = "mapDomainToDtoList")
  @Mapping(source = "orderProduct.menuProduct", target = "menuProduct", qualifiedByName = "mapDomainToMenuProduct")
  OrderProductDTO toDto(OrderProduct orderProduct);

  @Named("mapDtoMenuProductToDomain")
  default MenuProduct mapDtoMenuProductToDomain(MenuProductDTO menuProductDTO) {
    MenuProductMapperApp menuProductMapperApp = new MenuProductMapperAppImpl();
    return menuProductMapperApp.toDomain(menuProductDTO);
  }

  @Named("mapDomainToMenuProduct")
  default MenuProductDTO mapDomainToMenuProduct(MenuProduct menuProduct) {
    MenuProductMapperApp menuProductMapperApp = new MenuProductMapperAppImpl();
    return menuProductMapperApp.toMenuProductDTO(menuProduct);
  }

  @Named("mapDomainToDtoList")
  default List<OrderProductDTO> mapDomainToDtoList(List<OrderProduct> orderProducts) {
    return Optional.ofNullable(orderProducts).orElse(new ArrayList<>()).stream().map(this::toDto).toList();
  }

  @Mapping(source = "dto.ingredients", target = "ingredients", qualifiedByName = "mapDtoListToDomain")
  @Mapping(source = "dto.optionals", target = "optionals", qualifiedByName = "mapDtoListToDomain")
  @Mapping(source = "dto.menuProduct", target = "menuProduct", qualifiedByName = "mapDtoMenuProductToDomain")
  OrderProduct toDomain(OrderProductDTO dto);

  @Named("mapDtoListToDomain")
  default List<OrderProduct> mapDtoListToDomain(List<OrderProductDTO> dtoList) {
    return Optional.ofNullable(dtoList).orElse(new ArrayList<>()).stream().map(this::toDomain).collect(
        Collectors.toList());
  }

}
