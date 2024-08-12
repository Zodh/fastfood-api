package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MenuProductMapperInfra {

    @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapListToDomain")
    MenuProductDTO toDTO(MenuProductEntity entity);

    @Named("mapListToDomain")
    default List<MenuProductDTO> mapListToDomain(List<MenuProductEntity> menuProductEntities) {
        if (CollectionUtils.isEmpty(menuProductEntities)) {
            return new ArrayList<>();
        }
        return menuProductEntities.stream().map(this::toDTO).collect(Collectors.toList());
    }

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
