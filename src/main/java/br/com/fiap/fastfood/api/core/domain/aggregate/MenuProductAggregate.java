package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class MenuProductAggregate {

    private MenuProduct menuProduct;

    private MenuProductRepositoryPort menuProductRepository;

    public MenuProductAggregate(MenuProductRepositoryPort menuProductRepositoryPort) {
        this.menuProductRepository = menuProductRepository;
    }

    public List<MenuProduct> getAll() {
        List<MenuProduct> persistedProducts = menuProductRepository.getAll();

        if (persistedProducts.isEmpty()) {
            ErrorDetail errorDetail = new ErrorDetail("product", "Não há registros de produtos cadastrados!");
            throw new DomainException(errorDetail);
        }

        return persistedProducts;
    }

    public MenuProduct getById(Long id) {
        Optional<MenuProduct> persistedProduct = menuProductRepository.findById(id);

        if (persistedProduct.isEmpty()) {
            ErrorDetail errorDetail = new ErrorDetail("product", "Não existe um produto cadastrado com esse id!");
            throw new DomainException(errorDetail);
        }

        return persistedProduct.get();
    }

    public void register(MenuProduct menuProduct) {
        // Criar validações
        menuProductRepository.save(menuProduct);
    }
}