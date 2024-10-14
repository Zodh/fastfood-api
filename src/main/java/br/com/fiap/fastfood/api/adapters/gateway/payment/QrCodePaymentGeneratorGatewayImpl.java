package br.com.fiap.fastfood.api.adapters.gateway.payment;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class QrCodePaymentGeneratorGatewayImpl implements PaymentGeneratorGateway<BufferedImage, CreatePaymentRequest>{


  private final RestTemplate restTemplate;

  @Override
  public BufferedImage generate(CreatePaymentRequest request, String targetUrl) {
    RequestEntity<CreatePaymentRequest> reqEntity = null;
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.put(HttpHeaders.ACCEPT, List.of(MediaType.IMAGE_JPEG_VALUE));
    try {
      reqEntity = new RequestEntity<>(request, httpHeaders, HttpMethod.POST, new URI(targetUrl));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    byte[] responseData = restTemplate.exchange(
        reqEntity,
        byte[].class
    ).getBody();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(responseData);
    try {
      return ImageIO.read(byteArrayInputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
