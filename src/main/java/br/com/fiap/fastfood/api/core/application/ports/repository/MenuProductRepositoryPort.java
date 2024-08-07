package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuProductRepositoryPort extends BaseRepository<MenuProduct, Long> {

    Page<MenuProduct> getAll(Pageable pageable);
    void update(MenuProduct menuProduct);
    List<Long> fetchProductsRelatedToProduct(Long productId);
    List<MenuProduct> findAllById(List<Long> ids);

}
