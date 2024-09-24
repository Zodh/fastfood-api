package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.infrastructure.dao.entity.product.MenuProductEntity;
import br.com.fiap.fastfood.api.adapters.mapper.MenuProductMapperInfra;
import br.com.fiap.fastfood.api.infrastructure.dao.repository.product.MenuProductRepository;
import br.com.fiap.fastfood.api.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.application.gateway.repository.IMenuProductRepositoryGateway;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuProductRepositoryGatewayImpl implements IMenuProductRepositoryGateway {

  private final MenuProductRepository repository;
  private final MenuProductMapperInfra mapper;

  @Autowired
  public MenuProductRepositoryGatewayImpl(MenuProductRepository repository,
                                          MenuProductMapperInfra mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<MenuProductDTO> findById(Long identifier) {
    Optional<MenuProductEntity> menuProductEntityOpt = repository.findByIdAndActiveIsTrue(identifier);
    return menuProductEntityOpt.map(mapper::toDTO);
  }

  @Override
  public MenuProductDTO save(MenuProductDTO data) {
    MenuProductEntity entity = mapper.toEntity(data);
    MenuProductEntity persistedMenuProduct = repository.save(entity);
    return mapper.toDTO(persistedMenuProduct);
  }

  @Override
  public boolean delete(Long identifier) {
    Optional<MenuProductEntity> menuProductEntityOpt = repository.findById(identifier);
    if (menuProductEntityOpt.isEmpty()) {
      return false;
    }
    repository.updateActive(identifier, false);
    return true;
  }

  @Override
  public List<MenuProductDTO> getAll() {
    List<MenuProductEntity> entityProducts = repository.findAll();
    return entityProducts.stream().map(mapper::toDTO).toList();
  }

  @Override
  public void update(MenuProductDTO menuProduct) {
    MenuProductEntity entity = mapper.toEntity(menuProduct);
    repository.save(entity);
  }

  @Override
  public List<Long> fetchProductsRelatedToProduct(Long productId) {
    return repository.fetchProductsByIngredient(productId).stream().distinct()
        .collect(Collectors.toList());
  }

  @Override
  public List<MenuProductDTO> findAllById(List<Long> ids) {
    List<MenuProductEntity> products = repository.findAllById(ids);
    return products.stream().map(mapper::toDTO).collect(Collectors.toList());
  }

}
