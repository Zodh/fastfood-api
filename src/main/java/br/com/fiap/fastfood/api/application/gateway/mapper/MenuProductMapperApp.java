package br.com.fiap.fastfood.api.application.gateway.mapper;

import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import java.util.List;

import br.com.fiap.fastfood.api.entities.product.MenuProduct;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MenuProductMapperApp {

  MenuProduct toDomain(MenuProductDTO dto);

  List<MenuProductDTO> toMenuProductDTO(List<MenuProduct> menuProduct);

  MenuProductDTO toMenuProductDTO(MenuProduct menuProduct);

}
