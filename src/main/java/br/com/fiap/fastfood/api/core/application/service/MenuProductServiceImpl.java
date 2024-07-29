package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.domain.aggregate.MenuProductAggregate;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.MenuProductServicePort;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.MenuProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuProductServiceImpl implements MenuProductServicePort {

    private final MenuProductRepositoryPort menuProductRepositoryPort;
    private final MenuProductService menuProductService;

    @Autowired
    public MenuProductServiceImpl(
            MenuProductRepositoryPort menuProductRepositoryPort,
        MenuProductService menuProductService
    ) {
        this.menuProductRepositoryPort = menuProductRepositoryPort;
        this.menuProductService = menuProductService;
    }

    @Override
    public List<MenuProduct> getAll() {
        return menuProductService.getAll();
    }

    @Override
    public MenuProduct getById(Long id) {
        return menuProductService.getById(id);
    }

    @Override
    public void register(MenuProduct menuProduct) {
        MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProduct, menuProductRepositoryPort, menuProductService);
        menuProductAggregate.create();
    }

    @Override
    public void remove(Long id) {
        MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProductRepositoryPort, menuProductService);
        menuProductAggregate.remove(id);
    }

    @Override
    public void update(Long id, MenuProduct menuProduct) {
        MenuProductAggregate menuProductAggregate = new MenuProductAggregate(menuProduct, menuProductRepositoryPort, menuProductService);
        menuProductAggregate.update(id);
    }

}