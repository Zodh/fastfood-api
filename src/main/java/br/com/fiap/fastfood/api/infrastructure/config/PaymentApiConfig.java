package br.com.fiap.fastfood.api.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentApiConfig {

  @Value("${payment.api.host}")
  private String paymentUrl;

  public String getPaymentUrl() {
    return paymentUrl;
  }

}
