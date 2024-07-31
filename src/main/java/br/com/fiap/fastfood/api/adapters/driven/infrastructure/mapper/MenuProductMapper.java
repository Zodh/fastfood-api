package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driver.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MenuProductMapper {

    @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapListToDomain")
    MenuProduct toDomain(MenuProductEntity entity);

    MenuProduct toDomain(MenuProductDTO dto);

    List<MenuProductDTO> toMenuProductDTO(List<MenuProduct> menuProduct);

    MenuProductDTO toMenuProductDTO(MenuProduct menuProduct);

    @Named("mapListToDomain")
    default List<MenuProduct> mapListToDomain(List<MenuProductEntity> menuProductEntities) {
        if (CollectionUtils.isEmpty(menuProductEntities)) {
            return new ArrayList<>();
        }
        return menuProductEntities.stream().map(this::toDomain).collect(Collectors.toList());
    }

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
