package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;

import java.util.List;

public interface IMenuProductRepositoryGateway extends BaseRepository<MenuProductDTO, Long> {

    List<MenuProductDTO> getAll();
    void update(MenuProductDTO menuProduct);
    List<Long> fetchProductsRelatedToProduct(Long productId);
    List<MenuProductDTO> findAllById(List<Long> ids);

}
