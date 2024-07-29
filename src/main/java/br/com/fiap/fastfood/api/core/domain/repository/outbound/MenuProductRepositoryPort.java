package br.com.fiap.fastfood.api.core.domain.repository.outbound;

import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.repository.BaseRepository;

import java.util.List;

public interface MenuProductRepositoryPort extends BaseRepository<MenuProduct, Long> {

    List<MenuProduct> getAll();
    void update(MenuProduct menuProduct);
}
