package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProductValidator;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import java.util.List;
import lombok.Data;

import java.util.Optional;
import org.springframework.util.CollectionUtils;

@Data
public class MenuProductAggregate {

    private MenuProduct menuProduct;
    private MenuProductRepositoryPort menuProductRepository;
    private MenuProductValidator menuProductValidator;

    public MenuProductAggregate(MenuProductRepositoryPort menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    public MenuProductAggregate(MenuProduct menuProduct, MenuProductRepositoryPort menuProductRepositoryPort) {
        this.menuProduct = menuProduct;
        this.menuProductRepository = menuProductRepositoryPort;
        this.menuProductValidator = new MenuProductValidator();
    }

    public MenuProduct getById(Long id) {
        Optional<MenuProduct> persistedProduct = menuProductRepository.findById(id);
        return persistedProduct.orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado nenhum produto com o id %d", id)));
    }

    public void create() {
        List<ErrorDetail> errors = menuProductValidator.validate(menuProduct);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new DomainException(errors);
        }
        menuProductRepository.save(menuProduct);
    }

    public void remove(Long id) {
        Optional<MenuProduct> menuProductOpt = menuProductRepository.findById(menuProduct.getId());
        if (menuProductOpt.isEmpty()) {
            throw new NotFoundException(String.format("Não foi encontrado um produto com o identificador %d!", id));
        }
        menuProductRepository.delete(id);
    }
}