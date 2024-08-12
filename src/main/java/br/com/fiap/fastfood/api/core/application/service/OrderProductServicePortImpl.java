package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.MenuProductDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.mapper.MenuProductMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.MenuProductMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.OrderProductMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.OrderProductMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.product.MenuProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProductValidator;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.MenuProductServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderProductServicePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderProductServicePortImpl implements OrderProductServicePort {

    private final MenuProductServicePort menuProductServicePort;
    private final OrderProductRepositoryPort orderProductRepository;
    private final OrderProductValidator orderProductValidator = new OrderProductValidator();
    private final OrderProductMapperApp orderProductMapperApp;
    private final MenuProductMapperApp menuProductMapperApp;

    public OrderProductServicePortImpl(OrderProductRepositoryPort orderProductRepository, MenuProductServicePort menuProductServicePort) {
        this.orderProductRepository = orderProductRepository;
        this.menuProductServicePort = menuProductServicePort;
        this.orderProductMapperApp = new OrderProductMapperAppImpl();
        this.menuProductMapperApp = new MenuProductMapperAppImpl();
    }

    @Override
    public OrderProductDTO create(OrderDTO orderDTO, OrderProductDTO orderProductDTO) {
        OrderProductDTO validOrderProductDTO = validateAndDetail(orderProductDTO);
        validOrderProductDTO.setIngredients(validOrderProductDTO.getIngredients().stream().map(ingredient -> orderProductRepository.save(null, ingredient)).collect(Collectors.toList()));
        if (Objects.nonNull(validOrderProductDTO.getOptionals()) && !validOrderProductDTO.getOptionals().isEmpty()) {
            validOrderProductDTO.setOptionals(validOrderProductDTO.getOptionals().stream().map(optional -> orderProductRepository.save(null, optional)).collect(Collectors.toList()));
        }
        return orderProductRepository.save(orderDTO, validOrderProductDTO);
    }

    @Override
    public OrderProductDTO includeOptional(OrderDTO orderDTO, Long orderProductId, OrderProductDTO optional) {
        OrderProductDTO orderProductDTO = getById(orderProductId);
        OrderProductDTO validOptionalDTO = validateAndDetail(optional);
        if (!validOptionalDTO.isOptional()) {
            throw new DomainException(new ErrorDetail("optional", "O produto selecionado não é um opcional!"));
        }
        OrderProduct validOptional = orderProductMapperApp.toDomain(validOptionalDTO);
        validOptional.calculateCost();
        validOptional.calculatePrice();
        OrderProductDTO updatedValidOptional = orderProductMapperApp.toDto(validOptional);
        OrderProductDTO persistedOptional = orderProductRepository.save(updatedValidOptional);
        validOptional.setId(persistedOptional.getId());

        OrderProduct orderProduct = orderProductMapperApp.toDomain(orderProductDTO);
        orderProduct.includeOptional(validOptional);
        orderProduct.calculateCost();
        orderProduct.calculatePrice();

        OrderProductDTO updatedOrderProduct = orderProductMapperApp.toDto(orderProduct);
        orderProductRepository.save(orderDTO, updatedOrderProduct);
        return updatedOrderProduct;
    }

    @Override
    public OrderProductDTO removeOptional(OrderDTO orderDTO, Long orderProductId, Long optionalId) {
        OrderProductDTO orderProductDTO = getById(orderProductId);
        OrderProduct orderProduct = orderProductMapperApp.toDomain(orderProductDTO);

        OrderProduct optional = orderProduct.findOptionalById(optionalId);
        orderProduct.removeOptional(optionalId);
        orderProduct.calculatePrice();
        orderProduct.calculateCost();
        this.delete(optional.getId());

        OrderProductDTO calculated = orderProductMapperApp.toDto(orderProduct);
        return orderProductRepository.save(orderDTO, calculated);
    }

    @Override
    public OrderProductDTO updateShouldRemove(OrderDTO orderDTO, Long orderProductId, Long ingredientId, boolean shouldRemove) {
        OrderProductDTO orderProductDTO = getById(orderProductId);
        OrderProduct orderProduct = orderProductMapperApp.toDomain(orderProductDTO);
        OrderProduct ingredient = orderProduct.findIngredientById(ingredientId);
        ingredient.setShouldRemove(shouldRemove);
        orderProduct.calculateCost();
        orderProduct.calculatePrice();
        OrderProductDTO orderProductWithIngredientForRemoval = orderProductMapperApp.toDto(orderProduct);

        // TODO: validar atualização em cascata de ingredientes. Criado em: 02/08/2024 ás 05:31:34.
        return orderProductRepository.save(orderDTO, orderProductWithIngredientForRemoval);
    }

    @Override
    public void delete(Long id) {
        OrderProductDTO orderProduct = getById(id);
        List<Long> allOrderProductIdsToDelete = new ArrayList<>();
        allOrderProductIdsToDelete.add(id);
        if (Objects.nonNull(orderProduct.getIngredients()) && !orderProduct.getIngredients().isEmpty()) {
            List<Long> ingredients = orderProduct.getIngredients().stream().filter(i -> Objects.nonNull(i) && Objects.nonNull(i.getId())).map(
                OrderProductDTO::getId).toList();
            orderProductRepository.deleteIngredients(ingredients);
            allOrderProductIdsToDelete.addAll(ingredients);
        }
        if (Objects.nonNull(orderProduct.getOptionals()) && !orderProduct.getOptionals().isEmpty()) {
            List<Long> optionals = orderProduct.getOptionals().stream().filter(o -> Objects.nonNull(o) && Objects.nonNull(o.getId())).map(
                OrderProductDTO::getId).toList();
            orderProductRepository.deleteOptionals(optionals);
            allOrderProductIdsToDelete.addAll(optionals);
        }
        if (orderProduct.isOptional()) {
            orderProductRepository.deleteOptionals(List.of(orderProduct.getId()));
        }
        if (orderProduct.isIngredient()) {
            orderProductRepository.deleteIngredients(List.of(orderProduct.getId()));
        }
        orderProductRepository.deleteAllById(allOrderProductIdsToDelete);
    }

    @Override
    public OrderProductDTO getById(Long id) {
        return orderProductRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                String.format("Não foi encontrado nenhum produto do pedido com o identificador %d!",
                    id)));
    }

    @Override
    public OrderProductDTO validateAndDetail(OrderProductDTO orderProductDTO) {
        orderProductDTO = findMenuProductAndClone(orderProductDTO);
        OrderProduct initialOrderProduct = orderProductMapperApp.toDomain(orderProductDTO);

        List<ErrorDetail> errors = orderProductValidator.validate(initialOrderProduct);
        if (Objects.nonNull(errors) && !errors.isEmpty()) {
            throw new DomainException(errors);
        }
        fetchOptionalsData(orderProductDTO);

        OrderProduct orderProduct = orderProductMapperApp.toDomain(orderProductDTO);
        orderProduct.cloneMenuProduct();
        orderProduct.calculateCost();
        orderProduct.calculatePrice();

        return orderProductMapperApp.toDto(orderProduct);
    }

    @Override
    public OrderProductDTO save(OrderProductDTO orderProductDTO) {
        return orderProductRepository.save(orderProductDTO);
    }

    @Override
    public OrderProductDTO save(OrderDTO orderDTO, OrderProductDTO orderProductDTO) {
        return orderProductRepository.save(orderDTO, orderProductDTO);
    }

    private OrderProductDTO findMenuProductAndClone(OrderProductDTO orderProductDTO) {
        if (Objects.isNull(orderProductDTO) || Objects.isNull(
            orderProductDTO.getMenuProduct()) || orderProductDTO.getMenuProduct().getId() <= 0) {
            throw new DomainException(new ErrorDetail("product",
                "O produto do menu escolhido deve ser informado!"));
        }

        MenuProductDTO menuProductDTO = menuProductServicePort.getById(orderProductDTO.getMenuProduct().getId());
        orderProductDTO.setMenuProduct(menuProductDTO);

        OrderProduct orderProduct = orderProductMapperApp.toDomain(orderProductDTO);

        orderProduct.cloneMenuProduct();

        return orderProductMapperApp.toDto(orderProduct);
    }

    private void fetchOptionalsData(OrderProductDTO orderProductDTO) {
        OrderProduct orderProduct = orderProductMapperApp.toDomain(orderProductDTO);
        if (Objects.nonNull(orderProduct.getOptionals()) && !orderProduct.getOptionals().isEmpty()) {
            List<Long> ids = orderProduct.getOptionals().stream().map(op -> op.getMenuProduct().getId()).toList();
            Map<Long, MenuProduct> optionalsById = menuProductServicePort.findAllById(ids).stream().collect(Collectors.toMap(
                MenuProductDTO::getId, menuProductMapperApp::toDomain));
            orderProduct.getOptionals().forEach(op -> {
                op.setMenuProduct(optionalsById.get(op.getMenuProduct().getId()));
                op.cloneMenuProduct();
                op.calculateCost();
                op.calculatePrice();
            });
            List<OrderProductDTO> detailedOptionals = orderProductMapperApp.mapDomainToDtoList(orderProduct.getOptionals());
            orderProductDTO.setOptionals(detailedOptionals);
        }
    }

}
