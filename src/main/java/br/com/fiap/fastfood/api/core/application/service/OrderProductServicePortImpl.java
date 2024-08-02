package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.ports.repository.OrderProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProductValidator;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.MenuProductServicePort;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderProductServicePort;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
public class OrderProductServicePortImpl implements OrderProductServicePort {

    private final MenuProductServicePort menuProductServicePort;
    private final OrderProductValidator orderProductValidator = new OrderProductValidator();
    private final OrderProductRepositoryPort orderProductRepository;

    @Autowired
    public OrderProductServicePortImpl(OrderProductRepositoryPort orderProductRepository, MenuProductServicePort menuProductServicePort) {
        this.orderProductRepository = orderProductRepository;
        this.menuProductServicePort = menuProductServicePort;
    }

    @Override
    public OrderProduct create(OrderProduct orderProduct) {
        if (Objects.isNull(orderProduct) || Objects.isNull(orderProduct.getMenuProduct()) || orderProduct.getMenuProduct().getId() <= 0) {
            throw new DomainException(new ErrorDetail("product",
                "O produto do menu escolhido deve ser informado!"));
        }

        MenuProduct menuProduct = menuProductServicePort.getById(orderProduct.getMenuProduct().getId());
        orderProduct.setMenuProduct(menuProduct);

        orderProduct.cloneMenuProduct();
        List<ErrorDetail> errors = orderProductValidator.validate(orderProduct);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new DomainException(errors);
        }
        orderProduct.calculateCost();
        orderProduct.calculatePrice();
        return orderProductRepository.save(orderProduct);
    }

    @Override
    public OrderProduct includeOptional(Long orderProductId, OrderProduct optional) {
        OrderProduct orderProduct = getById(orderProductId);
        List<ErrorDetail> errors = orderProductValidator.validate(optional);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new DomainException(errors);
        }
        orderProduct.includeOptional(optional);
        optional.calculateCost();
        optional.calculatePrice();
        orderProductRepository.save(orderProduct);
        return orderProduct;
    }

    @Override
    public OrderProduct removeOptional(Long orderProductId, Long optionalId) {
        OrderProduct orderProduct = getById(orderProductId);
        OrderProduct optional = orderProduct.findOptionalById(optionalId);
        orderProductRepository.delete(optional.getId());
        OrderProduct updatedWithoutCalculate = getById(orderProductId);
        updatedWithoutCalculate.calculatePrice();
        updatedWithoutCalculate.calculateCost();
        return orderProductRepository.save(updatedWithoutCalculate);
    }

    @Override
    public OrderProduct updateShouldRemove(Long orderProductId, Long ingredientId, boolean shouldRemove) {
        OrderProduct orderProduct = getById(orderProductId);
        OrderProduct ingredient = orderProduct.findIngredientById(ingredientId);
        ingredient.setShouldRemove(shouldRemove);
        orderProduct.calculateCost();
        orderProduct.calculatePrice();
        // TODO: validar atualização em cascata de ingredientes. Criado em: 02/08/2024 ás 05:31:34.
        return orderProductRepository.save(orderProduct);
    }

    @Override
    public void delete(Long id) {
        OrderProduct orderProduct = getById(id);
        orderProductRepository.delete(orderProduct.getId());
    }

    @Override
    public OrderProduct getById(Long id) {
        return orderProductRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                String.format("Não foi encontrado nenhum produto do pedido com o identificador %d!",
                    id)));
    }
}
