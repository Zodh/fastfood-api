package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderProductService {

    public OrderProduct create(OrderProduct orderProduct) {

        if (Objects.isNull(orderProduct)) {
            throw new NotFoundException("O produto do pedido não pode ser nulo!");
        }
        // ------------- Começo das duvidas --------------------
        // Ele deve chamar o MenuProductAggregate para validar o orderProduct.getMenuProduct() ou deve instanciar o ProductValidator em um OrderAggregate?

        // Estou confuso em:
        // > A variavel orderProduct extende de Product, então orderProduct.getPrice será o valor total do pedido? Somando o menuProduct e todos os opcionais e ingredientes?
        // E o orderProduct.getCost será o custo total do menuProduct + todos os opcionais e ingredientes?

        // E pra finalizar, a orderProduct.optionals é uma List<OrderProduct>, cada opcional estará dentro de orderProduct.optionals.get(0).menuProduct.getPrice() ou dentro de orderProduct.optionals.get(0).getPrice()?
        // ------------- Fim das duvidas --------------------

        // Utilizarei o orderProduct.setPrice() para somar o preço total do produto do pedido + ingredientes + opcionais
        orderProduct.setPrice(orderProduct.getPrice());

        // Utilizarei o orderProduct.setCost() para somar o custo total do produto do pedido + ingredientes + opcionais
        orderProduct.setCost(orderProduct.getPrice());

        // Por fim, deverá salvar o orderProduct no Order.setOrderProducts();
        return orderProduct;
    }
}
