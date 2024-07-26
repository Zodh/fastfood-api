package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.driver.dto.MenuProductDTO;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MenuProductMapper {

    @Mapping(source = "entity.optionals", target = "optionals", qualifiedByName = "mapMenuProductList")
    @Mapping(source = "entity.ingredients", target = "ingredients", qualifiedByName = "mapMenuProductList")
    MenuProduct toDomain(MenuProductEntity entity);

    List<MenuProductDTO> toMenuProductDTO(List<MenuProduct> menuProduct);

    MenuProductDTO toMenuProductDTO(MenuProduct menuProduct);

    @Named("mapMenuProductList")
    default List<MenuProduct> mapMenuProductList(List<MenuProductEntity> ingredients) {
        if (CollectionUtils.isEmpty(ingredients)) {
            return new ArrayList<>();
        }
        return ingredients.stream().map(this::toDomain).collect(Collectors.toList());
    }
}
