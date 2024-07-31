package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;

import java.util.List;

public interface MenuProductRepositoryPort extends BaseRepository<MenuProduct, Long> {

    List<MenuProduct> getAll();
    void update(MenuProduct menuProduct);
    List<Long> fetchProductsRelatedToProduct(Long productId);
    List<MenuProduct> findAllById(List<Long> ids);

}
