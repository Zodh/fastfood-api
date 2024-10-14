package br.com.fiap.fastfood.api.adapters.gateway.payment;

public interface PaymentGeneratorGateway<RES, REQ> {

  RES generate(REQ request, String targetUrl);

}
