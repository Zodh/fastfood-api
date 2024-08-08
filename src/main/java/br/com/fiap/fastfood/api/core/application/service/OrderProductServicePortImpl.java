package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProductValidator;
import br.com.fiap.fastfood.api.core.domain.model.product.Product;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.MenuProductServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderProductServicePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderProductServicePortImpl implements OrderProductServicePort {

    private final MenuProductServicePort menuProductServicePort;
    private final OrderProductRepositoryPort orderProductRepository;
    private final OrderProductValidator orderProductValidator = new OrderProductValidator();

    public OrderProductServicePortImpl(OrderProductRepositoryPort orderProductRepository, MenuProductServicePort menuProductServicePort) {
        this.orderProductRepository = orderProductRepository;
        this.menuProductServicePort = menuProductServicePort;
    }

    @Override
    public OrderProduct create(Order order, OrderProduct orderProduct) {
        OrderProduct validOrderProduct = validateAndDetail(orderProduct);
        validOrderProduct.setIngredients(validOrderProduct.getIngredients().stream().map(ingredient -> orderProductRepository.save(null, ingredient)).collect(Collectors.toList()));
        if (Objects.nonNull(validOrderProduct.getOptionals()) && !validOrderProduct.getOptionals().isEmpty()) {
            validOrderProduct.setOptionals(validOrderProduct.getOptionals().stream().map(optional -> orderProductRepository.save(null, optional)).collect(Collectors.toList()));
        }
        return orderProductRepository.save(order, validOrderProduct);
    }

    @Override
    public OrderProduct includeOptional(Long orderProductId, OrderProduct optional) {
        OrderProduct orderProduct = getById(orderProductId);
        OrderProduct validOptional = validateAndDetail(optional);
        if (!validOptional.isOptional()) {
            throw new DomainException(new ErrorDetail("optional", "O produto selecionado não é um opcional!"));
        }
        orderProduct.includeOptional(validOptional);
        validOptional.calculateCost();
        validOptional.calculatePrice();

        orderProduct.calculateCost();
        orderProduct.calculatePrice();
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
        List<Long> allOrderProductIdsToDelete = new ArrayList<>();
        allOrderProductIdsToDelete.add(id);
        if (Objects.nonNull(orderProduct.getIngredients()) && !orderProduct.getIngredients().isEmpty()) {
            List<Long> ingredients = orderProduct.getIngredients().stream().filter(i -> Objects.nonNull(i) && Objects.nonNull(i.getId())).map(
                Product::getId).toList();
            orderProductRepository.deleteIngredients(ingredients);
            allOrderProductIdsToDelete.addAll(ingredients);
        }
        if (Objects.nonNull(orderProduct.getOptionals()) && !orderProduct.getOptionals().isEmpty()) {
            List<Long> optionals = orderProduct.getOptionals().stream().filter(o -> Objects.nonNull(o) && Objects.nonNull(o.getId())).map(
                Product::getId).toList();
            orderProductRepository.deleteOptionals(optionals);
            allOrderProductIdsToDelete.addAll(optionals);
        }
        orderProductRepository.deleteAllById(allOrderProductIdsToDelete);
    }

    @Override
    public OrderProduct getById(Long id) {
        return orderProductRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                String.format("Não foi encontrado nenhum produto do pedido com o identificador %d!",
                    id)));
    }

    @Override
    public OrderProduct validateAndDetail(OrderProduct orderProduct) {
        findMenuProductAndClone(orderProduct);

        List<ErrorDetail> errors = orderProductValidator.validate(orderProduct);
        if (Objects.nonNull(errors) && !errors.isEmpty()) {
            throw new DomainException(errors);
        }

        fetchOptionalsData(orderProduct);

        orderProduct.cloneMenuProduct();
        orderProduct.calculateCost();
        orderProduct.calculatePrice();
        return orderProduct;
    }

    private void findMenuProductAndClone(OrderProduct orderProduct) {
        if (Objects.isNull(orderProduct) || Objects.isNull(
            orderProduct.getMenuProduct()) || orderProduct.getMenuProduct().getId() <= 0) {
            throw new DomainException(new ErrorDetail("product",
                "O produto do menu escolhido deve ser informado!"));
        }

        MenuProduct menuProduct = menuProductServicePort.getById(orderProduct.getMenuProduct().getId());
        orderProduct.setMenuProduct(menuProduct);

        orderProduct.cloneMenuProduct();
    }

    private void fetchOptionalsData(OrderProduct orderProduct) {
        if (Objects.nonNull(orderProduct.getOptionals()) && !orderProduct.getOptionals().isEmpty()) {
            List<Long> ids = orderProduct.getOptionals().stream().map(op -> op.getMenuProduct().getId()).toList();
            Map<Long, MenuProduct> optionalsById = menuProductServicePort.findAllById(ids).stream().collect(Collectors.toMap(
                Product::getId, Function.identity()));
            orderProduct.getOptionals().forEach(op -> {
                op.setMenuProduct(optionalsById.get(op.getMenuProduct().getId()));
                op.cloneMenuProduct();
                op.calculateCost();
                op.calculatePrice();
            });
        }
    }

}
