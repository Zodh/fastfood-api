package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;

import java.util.List;

public interface IMenuProductRepositoryGateway extends BaseRepositoryGateway<MenuProductDTO, Long> {

    List<MenuProductDTO> getAll();
    void update(MenuProductDTO menuProduct);
    List<Long> fetchProductsRelatedToProduct(Long productId);
    List<MenuProductDTO> findAllById(List<Long> ids);

}
