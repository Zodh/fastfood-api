package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.domain.aggregate.MenuProductAggregate;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuProductService {

    private final MenuProductRepositoryPort menuProductRepositoryPort;

    @Autowired
    public MenuProductService(
            MenuProductRepositoryPort menuProductRepositoryPort
    ) {
        this.menuProductRepositoryPort = menuProductRepositoryPort;
    }

    public List<MenuProduct> getAll() {
        MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProductRepositoryPort);
        return menuProductAggregate.getAll();
    }

    public MenuProduct getById(Long id) {
        MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProductRepositoryPort);
        return menuProductAggregate.getById(id);
    }

    public void register(MenuProduct menuProduct) {
        MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProductRepositoryPort);
        menuProductAggregate.register(menuProduct);
    }
}