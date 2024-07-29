package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProductValidator;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.MenuProductRepositoryPort;
import java.util.List;
import java.util.Objects;
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
        fetchItems();
        menuProductRepository.save(menuProduct);
    }

    private void fetchItems() {
        if (Objects.nonNull(menuProduct) && !CollectionUtils.isEmpty(menuProduct.getIngredients())) {
            List<MenuProduct> ingredients = menuProduct.getIngredients().stream().map(i -> getById(i.getId())).toList();
            menuProduct.setIngredients(ingredients);
        }
        if (Objects.nonNull(menuProduct) && !CollectionUtils.isEmpty(menuProduct.getOptionals())) {
            List<MenuProduct> optionals = menuProduct.getIngredients().stream().map(i -> getById(i.getId())).toList();
            menuProduct.setOptionals(optionals);
        }
    }

    public void remove(Long id) {
        MenuProduct target = getById(id);
        List<Long> productsThatUseTarget = menuProductRepository.fetchProductsRelatedToProduct(target.getId());
        menuProductRepository.delete(target.getId());

        // After remove target product, we need to check if other product used it as optional or ingredient.
        List<MenuProduct> allProductsThatUsedRemovedProduct = menuProductRepository.findAllById(productsThatUseTarget);
        allProductsThatUsedRemovedProduct.forEach(p -> {
            p.setIngredients(p.getIngredients());
            p.setOptionals(p.getOptionals());
        });
        allProductsThatUsedRemovedProduct.forEach(menuProductRepository::update);
    }

    public void update(Long id) {
        MenuProduct current = getById(id);
        menuProduct.setId(current.getId());
        List<ErrorDetail> errors = menuProductValidator.validate(menuProduct);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new DomainException(errors);
        }
        fetchItems();
        menuProductRepository.update(menuProduct);
    }

}