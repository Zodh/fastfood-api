package br.com.fiap.fastfood.api.application.usecase;

import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.mapper.*;
import br.com.fiap.fastfood.api.core.application.policy.FollowUpPolicy;
import br.com.fiap.fastfood.api.core.application.policy.OrderInvoicePolicy;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.CustomerServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderProductServicePort;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.OrderServicePort;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.port.outbound.EmailSenderPort;
import br.com.fiap.fastfood.api.entities.invoice.Invoice;
import br.com.fiap.fastfood.api.entities.order.Order;
import br.com.fiap.fastfood.api.entities.order.state.impl.OrderInCreationState;
import br.com.fiap.fastfood.api.entities.person.Collaborator;
import br.com.fiap.fastfood.api.entities.person.Customer;
import br.com.fiap.fastfood.api.entities.product.OrderProduct;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderUseCase implements OrderServicePort {

  private final OrderRepositoryPort orderRepositoryPort;
  private final CustomerServicePort customerServicePort;
  private final OrderProductServicePort orderProductServicePort;
  private final OrderInvoicePolicy orderInvoicePolicy;
  private final OrderMapperApp orderMapperApp;
  private final OrderProductMapperApp orderProductMapperApp;
  private final CustomerMapperApp customerMapperApp;
  private final CollaboratorMapperApp collaboratorMapperApp;
  private final InvoiceMapperApp invoiceMapperApp;
  private final EmailSenderPort emailSenderPort;
  private final FollowUpPolicy followUpPolicy;

  public OrderUseCase(OrderRepositoryPort orderRepositoryPort, CustomerServicePort customerServicePort, OrderProductServicePort orderProductServicePort, OrderInvoicePolicy orderInvoicePolicy, EmailSenderPort emailSenderPort, FollowUpPolicy followUpPolicy) {
    this.orderRepositoryPort = orderRepositoryPort;
    this.customerServicePort = customerServicePort;
    this.orderProductServicePort = orderProductServicePort;
    this.orderInvoicePolicy = orderInvoicePolicy;
    this.orderMapperApp = new OrderMapperAppImpl();
    this.orderProductMapperApp = new OrderProductMapperAppImpl();
    this.customerMapperApp = new CustomerMapperAppImpl();
    this.collaboratorMapperApp = new CollaboratorMapperAppImpl();
    this.invoiceMapperApp = new InvoiceMapperAppImpl();
    this.emailSenderPort = emailSenderPort;
    this.followUpPolicy = followUpPolicy;
  }

  @Override
  public OrderDTO create(CustomerDTO customerDTO, CollaboratorDTO collaboratorDTO) {
    customerDTO = fetchCustomerData(customerDTO);
    Customer customer = customerMapperApp.toDomain(customerDTO);
    Collaborator collaborator = collaboratorMapperApp.toDomain(collaboratorDTO);

    Order order = new Order();
    order.changeState(new OrderInCreationState(order));
    order.getState().setCollaborator(collaborator);
    order.getState().setCustomer(customer);
    order.calculatePrice();
    order.setCreatedAt(LocalDateTime.now());

    OrderDTO validOrder = orderMapperApp.toDTO(order);
    return orderRepositoryPort.save(validOrder);
  }

  @Override
  public OrderDTO includeOrderProduct(Long orderId, OrderProductDTO orderProductDTO) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    OrderProductDTO detailedDTO = orderProductServicePort.create(orderDTO, orderProductDTO);
    OrderProduct detailed = orderProductMapperApp.toDomain(detailedDTO);

    order.getState().includeOrderProduct(detailed);
    order.calculatePrice();

    OrderDTO detailedOrderDTO = orderMapperApp.toDTO(order);
    return orderRepositoryPort.save(detailedOrderDTO);
  }

  @Override
  public OrderDTO getById(Long orderId) {
    OrderDTO orderDTO = orderRepositoryPort.findById(orderId).orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum pedido com o identificador informado!"));
    Order order = orderMapperApp.toDomain(orderDTO);
    Invoice activeInvoice = order.getActiveInvoiceOrNull();
    InvoiceDTO activeInvoiceDTO = invoiceMapperApp.toDto(activeInvoice);
    orderDTO.setInvoice(activeInvoiceDTO);
    return orderDTO;
  }

  @Override
  public OrderDTO removeOrderProduct(Long orderId, Long orderProductId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().removeOrderProduct(orderProductId);
    order.calculatePrice();

    orderProductServicePort.delete(orderProductId);
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    return orderRepositoryPort.save(updatedOrder);
  }

  @Override
  public void cancel(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().cancelOrder();

    OrderDTO updatedOrderDTO = orderMapperApp.toDTO(order);
    OrderDTO cancelledOrder = save(updatedOrderDTO);
    orderInvoicePolicy.cancelOrderInvoice(cancelledOrder);
  }

  @Override
  public OrderDTO confirm(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().confirmOrder();

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO confirmedOrder = save(updatedOrder);
    orderInvoicePolicy.generateOrderInvoice(confirmedOrder);
    return confirmedOrder;
  }

  @Override
  public OrderDTO includeOptional(Long orderId, Long orderProductId, OrderProductDTO optionalDTO) {
    // Fetch order and check if can add optional into it
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().includeOptionalInProduct();

    // Include optional
    orderProductServicePort.includeOptional(orderDTO, orderProductId, optionalDTO);

    // Fetch order with optional, update price and save.
    OrderDTO orderWithNewOptionalDTO = getById(orderId);
    Order orderWithNewOptional = orderMapperApp.toDomain(orderWithNewOptionalDTO);
    orderWithNewOptional.changeState(orderMapperApp.mapStateImpl(orderWithNewOptionalDTO.getState(), orderWithNewOptional));
    orderWithNewOptional.calculatePrice();
    OrderDTO result = orderMapperApp.toDTO(orderWithNewOptional);
    return save(result);
  }

  @Override
  public OrderDTO removeOptional(Long orderId, Long orderProductId, Long optionalId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().removeOptionalFromProduct();

    orderProductServicePort.removeOptional(orderDTO, orderProductId, optionalId);

    // Fetch order with optional, update price and save.
    OrderDTO orderWithoutOptionalDTO = getById(orderId);
    Order orderWithoutOptional = orderMapperApp.toDomain(orderWithoutOptionalDTO);
    orderWithoutOptional.changeState(orderMapperApp.mapStateImpl(orderWithoutOptionalDTO.getState(), orderWithoutOptional));
    orderWithoutOptional.calculatePrice();
    OrderDTO result = orderMapperApp.toDTO(orderWithoutOptional);
    return save(result);
  }

  @Override
  public OrderDTO updateShouldRemoveIngredient(Long orderId, Long orderProductId, Long ingredientId,
      boolean shouldRemove) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().updateIngredientRemoval(orderProductId, ingredientId, shouldRemove);

    // The ingredient doesnt contains the order reference in itself
    OrderProduct orderProduct = order.findProductById(orderProductId);
    OrderProductDTO orderProductDTO = orderProductMapperApp.toDto(orderProduct);

    OrderProduct ingredient = orderProduct.findIngredientById(ingredientId);
    OrderProductDTO ingredientDTO = orderProductMapperApp.toDto(ingredient);

    orderProductServicePort.save(ingredientDTO);

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persistedOrder = orderRepositoryPort.save(updatedOrder);
    orderProductServicePort.save(persistedOrder, orderProductDTO);

    return getById(orderId);
  }

  @Override
  public List<OrderDTO> findAll() {
    return orderRepositoryPort.findAll();
  }

  @Override
  public void prepare(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().initializePreparation();

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryPort.save(updatedOrder);
    followUpPolicy.updateOrderInFollowUp(persisted, FollowUpStateEnum.IN_PREPARATION);
  }

  @Override
  public void turnReadyToPick(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    this.sendEmailForReadyOrder(order);

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryPort.save(updatedOrder);
    followUpPolicy.updateOrderInFollowUp(persisted, FollowUpStateEnum.READY);
  }

  @Override
  public void finish(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().finish();

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryPort.save(updatedOrder);
    followUpPolicy.updateOrderInFollowUp(persisted, FollowUpStateEnum.FINISHED);
  }

  private OrderDTO save(OrderDTO orderDTO) {
    OrderDTO confirmedOrder = orderRepositoryPort.save(orderDTO);
    confirmedOrder.setInvoices(orderDTO.getInvoices());
    return confirmedOrder;
  }

  private CustomerDTO fetchCustomerData(CustomerDTO customer) {
    if (Objects.nonNull(customer)) {
      if (Objects.isNull(customer.getId()) && (StringUtils.isBlank(customer.getDocumentNumber()) || Objects.isNull(customer.getDocumentType()))) {
        throw new ApplicationException("O identificador ou o documento do cliente precisa ser informado para constar no pedido!", "Cliente sem identificadores válidos!");
      }
      if (Objects.nonNull(customer.getId())) {
        customer = customerServicePort.getById(customer.getId());
      }
      if (StringUtils.isNotBlank(customer.getDocumentNumber()) && Objects.nonNull(customer.getDocumentType())) {
        customer = customerServicePort.getByDocument(customer.getDocumentNumber(), customer.getDocumentType());
      }
    }
    return customer;
  }

  public void sendEmailForReadyOrder(Order order) {
    order.getState().setReadyToCollection();
    if (
            Objects.nonNull(order.getCustomer())
                    && Objects.nonNull(order.getCustomer().getEmail())
                    && StringUtils.isNotBlank(order.getCustomer().getEmail().getValue())
                    && StringUtils.isNotBlank(order.getCustomer().getFirstName())
    ) {
      final String subject = "Seu pedido está pronto!";
      final String message = String.format("""
          
          Olá, %s!
          
          O seu pedido Nº %d está pronto!
          
          Passe no balcão para retirá-lo!
          
          """, order.getCustomer().getFirstName(), order.getId());
      emailSenderPort.send(message, order.getCustomer().getEmail().getValue(), subject);
    }
  }

}
