package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.model.category.CategoryValidator;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CategoryRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.service.CategoryService;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class CategoryAggregate {

    private Category category;

    private CategoryRepositoryPort categoryRepository;
    private CategoryValidator categoryValidator;
    private CategoryService categoryService;

    public CategoryAggregate(Category category, CategoryRepositoryPort categoryRepositoryPort, CategoryService categoryService) {
        this.category = category;
        this.categoryRepository = categoryRepositoryPort;
        this.categoryValidator = new CategoryValidator();
        this.categoryService = categoryService;
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

        // Criar um update para atualizar a lista de produtos que utilizam essa categoria
        categoryRepository.update(category);
    }

    // remove

}
