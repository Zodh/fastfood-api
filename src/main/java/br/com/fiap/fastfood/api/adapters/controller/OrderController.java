package br.com.fiap.fastfood.api.adapters.controller;

import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeLinkGeneratorGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.EmailSenderGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.CustomerRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.FollowUpRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.InvoiceRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.MenuProductRepositoryGatewayImpl;
import br.com.fiap.fastfood.api.adapters.gateway.OrderProductRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.OrderRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.ActivationCodeRepositoryAdapterImpl;
import br.com.fiap.fastfood.api.adapters.gateway.link.ApplicationServerLinkGenerator;
import br.com.fiap.fastfood.api.adapters.gateway.payment.CreatePaymentRequest;
import br.com.fiap.fastfood.api.adapters.gateway.payment.PaymentGeneratorGateway;
import br.com.fiap.fastfood.api.adapters.gateway.payment.QrCodePaymentGeneratorGatewayImpl;
import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.application.dto.order.CreateOrderRequestDTO;
import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.application.dto.order.PaidOrderResponseDTO;
import br.com.fiap.fastfood.api.application.gateway.mapper.ActivationCodeMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.CollaboratorMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.CustomerMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.CustomerMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.FollowUpMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.InvoiceMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.InvoiceMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.MenuProductMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.MenuProductMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderMapperAppImpl;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderProductMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderProductMapperAppImpl;
import br.com.fiap.fastfood.api.application.usecase.impl.InvoiceUseCaseImpl;
import br.com.fiap.fastfood.api.application.usecase.impl.OrderUseCaseImpl;
import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.application.policy.FollowUpPolicyImpl;
import br.com.fiap.fastfood.api.application.policy.OrderInvoicePolicyImpl;
import br.com.fiap.fastfood.api.application.usecase.InvoiceUseCase;
import br.com.fiap.fastfood.api.application.usecase.OrderUseCase;
import br.com.fiap.fastfood.api.application.usecase.impl.CustomerUseCaseImpl;
import br.com.fiap.fastfood.api.application.service.impl.MenuProductServiceImpl;
import br.com.fiap.fastfood.api.application.service.impl.OrderProductServiceImpl;
import br.com.fiap.fastfood.api.infrastructure.config.PaymentApiConfig;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

public class OrderController {

  private final OrderUseCase orderUseCase;
  private final InvoiceUseCase invoiceUseCase;
  private final PaymentGeneratorGateway<BufferedImage, CreatePaymentRequest>  paymentGenerator;
  private final PaymentApiConfig paymentApiConfig;
  private final ApplicationServerLinkGenerator applicationServerLinkGenerator;

  public OrderController(
      OrderRepositoryAdapterImpl orderRepositoryAdapter,
      CustomerRepositoryAdapterImpl customerRepositoryAdapter,
      EmailSenderGatewayImpl emailSenderGatewayImplAdapter,
      ActivationCodeRepositoryAdapterImpl activationCodeRepositoryAdapter,
      ActivationCodeLinkGeneratorGatewayImpl activationCodeLinkGeneratorGatewayImpl,
      OrderProductRepositoryAdapterImpl orderProductRepositoryAdapter,
      MenuProductRepositoryGatewayImpl menuProductRepositoryAdapter,
      InvoiceRepositoryAdapterImpl invoiceRepositoryAdapter,
      FollowUpRepositoryAdapterImpl followUpRepositoryAdapter,
       RestTemplate restTemplate,
      PaymentApiConfig paymentApiConfig,
      ApplicationServerLinkGenerator applicationServerLinkGenerator
  ) {
    CustomerMapperApp customerMapperApp = new CustomerMapperAppImpl();
    CustomerUseCaseImpl customerUseCaseImpl = new CustomerUseCaseImpl(
        customerRepositoryAdapter, emailSenderGatewayImplAdapter, activationCodeRepositoryAdapter,
        activationCodeLinkGeneratorGatewayImpl, new ActivationCodeMapperAppImpl(), customerMapperApp);
    MenuProductMapperApp menuProductMapperApp = new MenuProductMapperAppImpl();
    InvoiceMapperApp invoiceMapperAppImpl = new InvoiceMapperAppImpl();
    OrderMapperApp orderMapperAppImpl = new OrderMapperAppImpl();
    OrderProductMapperApp orderProductMapperApp = new OrderProductMapperAppImpl();
    MenuProductServiceImpl menuProductServiceImpl = new MenuProductServiceImpl(
        menuProductRepositoryAdapter, menuProductMapperApp);
    OrderProductServiceImpl orderProductServicePort = new OrderProductServiceImpl(
        orderProductRepositoryAdapter, menuProductServiceImpl, menuProductMapperApp, orderProductMapperApp);
    FollowUpPolicyImpl followUpPolicy = new FollowUpPolicyImpl(followUpRepositoryAdapter, new FollowUpMapperAppImpl());
    OrderInvoicePolicyImpl orderInvoicePolicyPort = new OrderInvoicePolicyImpl(
        invoiceRepositoryAdapter, orderRepositoryAdapter, followUpPolicy, invoiceMapperAppImpl, orderMapperAppImpl);
    this.orderUseCase = new OrderUseCaseImpl(invoiceMapperAppImpl, new CollaboratorMapperAppImpl(), customerMapperApp, orderProductMapperApp, orderMapperAppImpl, orderRepositoryAdapter,
        customerUseCaseImpl, orderProductServicePort, orderInvoicePolicyPort,
        emailSenderGatewayImplAdapter, followUpPolicy);
    this.invoiceUseCase = new InvoiceUseCaseImpl(invoiceRepositoryAdapter,
        orderInvoicePolicyPort, invoiceMapperAppImpl, orderMapperAppImpl);
    this.paymentGenerator = new QrCodePaymentGeneratorGatewayImpl(restTemplate);
    this.paymentApiConfig = paymentApiConfig;
    this.applicationServerLinkGenerator = applicationServerLinkGenerator;
  }


  public List<OrderDTO> findAll() {
    return orderUseCase.findAll();
  }


  public OrderDTO create(CreateOrderRequestDTO request) {
    OrderDTO orderDTO = orderUseCase.create(request.getCustomer(), request.getCollaborator());
    return orderDTO;
  }


  public OrderDTO includeOrderProduct(Long id, OrderProductDTO orderProductDTO) {
    OrderDTO order = orderUseCase.includeOrderProduct(id, orderProductDTO);
    return order;
  }

  public OrderDTO removeOrderProduct(Long id, Long orderProductId) {
    OrderDTO orderDTO = orderUseCase.removeOrderProduct(id, orderProductId);
    return orderDTO;
  }

  public BufferedImage confirmOrder(@PathVariable Long id) {
    OrderDTO confirmedOrder = orderUseCase.confirm(id);
    CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(
        confirmedOrder.getInvoice().getId(),
        confirmedOrder.getId(),
        confirmedOrder.getPrice(),
        "BRL",
        confirmedOrder.getProducts().stream().filter(co -> Objects.nonNull(co) && StringUtils.isNotBlank(co.getName())).map(co -> String.format("%dx - %s", co.getQuantity(), co.getName())).collect(
            Collectors.joining(", ")), applicationServerLinkGenerator.generate() + String.format("/orders/%d/pay", confirmedOrder.getId()), null);
    return paymentGenerator.generate(createPaymentRequest, paymentApiConfig.getPaymentUrl());
  }
  
  public PaidOrderResponseDTO executeFakeCheckout(Long id) {
    OrderDTO orderDTO = orderUseCase.getById(id);
    invoiceUseCase.executeFakeCheckout(orderDTO);
    PaidOrderResponseDTO result = PaidOrderResponseDTO.builder().orderNumber(orderDTO.getId())
        .build();
    return result;
  }

  public InvoiceDTO getOrderInvoice(Long id) {
    InvoiceDTO orderInvoice = orderUseCase.getById(id).getInvoice();
    return orderInvoice;
  }


  public void cancel(Long id) {
    orderUseCase.cancel(id);
  }


  public void startOrderPreparation( Long id) {
    orderUseCase.prepare(id);
  }

  public void turnOrderReadyToPick(Long id) {
    orderUseCase.turnReadyToPick(id);
  }


  public void finishOrder(Long id) {
    orderUseCase.finish(id);
  }


  public OrderDTO includeOptional(Long id, Long orderProductId, OrderProductDTO optionalDTO) {
    OrderDTO result = orderUseCase.includeOptional(id, orderProductId, optionalDTO);
    return result;
  }

  public OrderDTO removeOptional(Long id,Long orderProductId,Long optionalId) {
    OrderDTO orderDTO = orderUseCase.removeOptional(id, orderProductId, optionalId);
    return orderDTO;
  }


  public OrderDTO updateShouldRemove(Long id,Long orderProductId,Long ingredientId,boolean shouldRemove) {
    OrderDTO orderDTO = orderUseCase.updateShouldRemoveIngredient(id, orderProductId, ingredientId, shouldRemove);
    return orderDTO;
  }

}
