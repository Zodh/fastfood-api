package br.com.fiap.fastfood.api.core.domain.service;

import br.com.fiap.fastfood.api.core.domain.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.model.category.Category;
import br.com.fiap.fastfood.api.core.domain.repository.outbound.CategoryRepositoryPort;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    private final CategoryRepositoryPort categoryRepositoryPort;

    public CategoryService(CategoryRepositoryPort categoryRepositoryPort) {
        this.categoryRepositoryPort = categoryRepositoryPort;
    }

    public List<Category> getAll() {
        return categoryRepositoryPort.getAll();
    }

    public Category getById(Long id) {
        Optional<Category> persistedCategory = categoryRepositoryPort.findById(id);
        return persistedCategory.orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado nenhuma categoria com o id %d", id)));
    }

    public Category getByName(String name) {
        Optional<Category> persistedCategory = categoryRepositoryPort.findByName(name);
        return persistedCategory.orElseThrow(() -> new NotFoundException(String.format("Não foi encontrado nenhuma categoria com o nome %s", name)));
    }

}
