package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.model.category.CategoryValidator;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.CategoryService;
import br.com.fiap.fastfood.api.core.domain.service.MenuProductService;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Data
public class CategoryAggregate {

    private Category category;

    private CategoryRepositoryPort categoryRepository;
    private CategoryValidator categoryValidator;
    private CategoryService categoryService;
    private MenuProductService menuProductService;

    public CategoryAggregate(CategoryRepositoryPort categoryRepositoryPort, CategoryService categoryService) {
        this.categoryRepository = categoryRepositoryPort;
        this.categoryService = categoryService;
    }

    public CategoryAggregate(Category category, CategoryRepositoryPort categoryRepositoryPort, CategoryService categoryService) {
        this.category = category;
        this.categoryRepository = categoryRepositoryPort;
        this.categoryValidator = new CategoryValidator();
        this.categoryService = categoryService;
    }

    public CategoryAggregate(Category category, CategoryRepositoryPort categoryRepositoryPort, CategoryService categoryService, MenuProductService menuProductService) {
        this.category = category;
        this.categoryRepository = categoryRepositoryPort;
        this.categoryValidator = new CategoryValidator();
        this.categoryService = categoryService;
        this.menuProductService = menuProductService;
    }

    public void create() {
        List<ErrorDetail> errors = categoryValidator.validate(category);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new DomainException(errors);
        }

        categoryRepository.save(category);
    }

    public void update(Long id) {
        Category current = categoryService.getById(id);
        category.setId(current.getId());
        List<ErrorDetail> errors = categoryValidator.validate(category);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new DomainException(errors);
        }

        fetchProducts();
        categoryRepository.update(category);
    }

    public void remove(Long id) {
        Category target = categoryService.getById(id);
        List<Long> productsThatUseTarget = categoryRepository.fetchProductsRelatedToCategory(target.getId());

        //TODO Remover a associação dos produtos na categoria automaticamente?
        // Emitir o throw que não pode remover a categoria caso ela esteja associada a um ou mais produtos?
        // O produto pode ficar sem uma categoria definida?
        if (!CollectionUtils.isEmpty(productsThatUseTarget)) {
            throw new DomainException(new ErrorDetail("category", "A categoria não pode estar associada a um ou mais produtos para ser deletada!"));
        }

        categoryRepository.delete(target.getId());
    }

    private void fetchProducts() {
        if (Objects.nonNull(category) && !CollectionUtils.isEmpty(category.getProducts())) {
            List<MenuProduct> products = category.getProducts().stream().map(i -> menuProductService.getById(i.getId())).toList();
            category.setProducts(products);
        }
    }
}
