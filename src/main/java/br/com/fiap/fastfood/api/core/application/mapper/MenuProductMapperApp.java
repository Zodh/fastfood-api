package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MenuProductMapperApp {

  MenuProduct toDomain(MenuProductDTO dto);

  List<MenuProductDTO> toMenuProductDTO(List<MenuProduct> menuProduct);

  MenuProductDTO toMenuProductDTO(MenuProduct menuProduct);

}
