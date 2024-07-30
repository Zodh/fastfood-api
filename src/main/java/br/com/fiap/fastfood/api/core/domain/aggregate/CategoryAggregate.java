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
        fetchProducts();
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
        categoryRepository.delete(target.getId());
    }

    private void fetchProducts() {
        if (Objects.nonNull(category) && !CollectionUtils.isEmpty(category.getProducts())) {
            List<MenuProduct> products = category.getProducts().stream().map(menuProduct -> menuProductService.getById(menuProduct.getId())).toList();
            category.setProducts(products);
        }
    }
}
