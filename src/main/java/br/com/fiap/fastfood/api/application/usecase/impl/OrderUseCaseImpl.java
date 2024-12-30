package br.com.fiap.fastfood.api.application.usecase.impl;

import br.com.fiap.fastfood.api.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.application.gateway.mapper.CollaboratorMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.CustomerMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.InvoiceMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderProductMapperApp;
import br.com.fiap.fastfood.api.application.usecase.CustomerUseCase;
import br.com.fiap.fastfood.api.application.usecase.OrderUseCase;
import br.com.fiap.fastfood.api.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.application.policy.FollowUpPolicy;
import br.com.fiap.fastfood.api.application.policy.OrderInvoicePolicy;
import br.com.fiap.fastfood.api.application.service.OrderProductService;
import br.com.fiap.fastfood.api.application.gateway.repository.OrderRepositoryGateway;
import br.com.fiap.fastfood.api.application.gateway.EmailSenderGateway;
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

public class OrderUseCaseImpl implements
    OrderUseCase {

  private final OrderRepositoryGateway orderRepositoryGateway;
  private final CustomerUseCase customerUseCase;
  private final OrderProductService orderProductService;
  private final OrderInvoicePolicy orderInvoicePolicy;
  private final OrderMapperApp orderMapperApp;
  private final OrderProductMapperApp orderProductMapperApp;
  private final CustomerMapperApp customerMapperApp;
  private final CollaboratorMapperApp collaboratorMapperApp;
  private final InvoiceMapperApp invoiceMapperApp;
  private final EmailSenderGateway emailSenderGateway;
  private final FollowUpPolicy followUpPolicy;

  public OrderUseCaseImpl(InvoiceMapperApp invoiceMapperApp,
      CollaboratorMapperApp collaboratorMapperApp,
      CustomerMapperApp customerMapperApp,
      OrderProductMapperApp orderProductMapperApp,
      OrderMapperApp orderMapperApp,
      OrderRepositoryGateway orderRepositoryGateway,
      CustomerUseCase customerUseCase,
      OrderProductService orderProductService,
      OrderInvoicePolicy orderInvoicePolicy,
      EmailSenderGateway emailSenderGateway,
      FollowUpPolicy followUpPolicy
  ) {
    this.orderRepositoryGateway = orderRepositoryGateway;
    this.customerUseCase = customerUseCase;
    this.orderProductService = orderProductService;
    this.orderInvoicePolicy = orderInvoicePolicy;
    this.orderMapperApp = orderMapperApp;
    this.orderProductMapperApp = orderProductMapperApp;
    this.customerMapperApp = customerMapperApp;
    this.collaboratorMapperApp = collaboratorMapperApp;
    this.invoiceMapperApp = invoiceMapperApp;
    this.emailSenderGateway = emailSenderGateway;
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
    return orderRepositoryGateway.save(validOrder);
  }

  @Override
  public OrderDTO includeOrderProduct(Long orderId, OrderProductDTO orderProductDTO) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));
    OrderProductDTO detailedDTO = orderProductService.create(orderDTO, orderProductDTO);
    OrderProduct detailed = orderProductMapperApp.toDomain(detailedDTO);

    order.getState().includeOrderProduct(detailed);
    order.calculatePrice();

    OrderDTO detailedOrderDTO = orderMapperApp.toDTO(order);
    return orderRepositoryGateway.save(detailedOrderDTO);
  }

  @Override
  public OrderDTO getById(Long orderId) {
    OrderDTO orderDTO = orderRepositoryGateway.findById(orderId).orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum pedido com o identificador informado!"));
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

    orderProductService.delete(orderProductId);
    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    return orderRepositoryGateway.save(updatedOrder);
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
    orderProductService.includeOptional(orderDTO, orderProductId, optionalDTO);

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

    orderProductService.removeOptional(orderDTO, orderProductId, optionalId);

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

    orderProductService.save(ingredientDTO);

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persistedOrder = orderRepositoryGateway.save(updatedOrder);
    orderProductService.save(persistedOrder, orderProductDTO);

    return getById(orderId);
  }

  @Override
  public List<OrderDTO> findAll() {
    return orderRepositoryGateway.findAll();
  }

  @Override
  public void prepare(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().initializePreparation();

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryGateway.save(updatedOrder);
    followUpPolicy.updateOrderInFollowUp(persisted, FollowUpStateEnum.IN_PREPARATION);
  }

  @Override
  public void turnReadyToPick(Long orderId) {
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    this.sendEmailForReadyOrder(order);

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryGateway.save(updatedOrder);
    followUpPolicy.updateOrderInFollowUp(persisted, FollowUpStateEnum.READY);
  }

  @Override
  public void finish(Long orderId) {
    // Finish
    OrderDTO orderDTO = getById(orderId);
    Order order = orderMapperApp.toDomain(orderDTO);
    order.changeState(orderMapperApp.mapStateImpl(orderDTO.getState(), order));

    order.getState().finish();

    OrderDTO updatedOrder = orderMapperApp.toDTO(order);
    OrderDTO persisted = orderRepositoryGateway.save(updatedOrder);

    // Fire update follow up
    followUpPolicy.updateOrderInFollowUp(persisted, FollowUpStateEnum.FINISHED);
  }

  private OrderDTO save(OrderDTO orderDTO) {
    OrderDTO confirmedOrder = orderRepositoryGateway.save(orderDTO);
    confirmedOrder.setInvoices(orderDTO.getInvoices());
    return confirmedOrder;
  }

  private CustomerDTO fetchCustomerData(CustomerDTO customer) {
    if (Objects.nonNull(customer)) {
      if (Objects.isNull(customer.getId()) && (StringUtils.isBlank(customer.getDocumentNumber()) || Objects.isNull(customer.getDocumentType()))) {
        throw new ApplicationException("O identificador ou o documento do cliente precisa ser informado para constar no pedido!", "Cliente sem identificadores válidos!");
      }
      if (Objects.nonNull(customer.getId())) {
        customer = customerUseCase.getById(customer.getId());
      }
      if (StringUtils.isNotBlank(customer.getDocumentNumber()) && Objects.nonNull(customer.getDocumentType())) {
        customer = customerUseCase.getByDocument(customer.getDocumentNumber(), customer.getDocumentType());
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
      emailSenderGateway.send(message, order.getCustomer().getEmail().getValue(), subject);
    }
  }

}
