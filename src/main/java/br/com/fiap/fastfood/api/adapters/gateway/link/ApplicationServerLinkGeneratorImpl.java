package br.com.fiap.fastfood.api.adapters.gateway.link;

import br.com.fiap.fastfood.api.utils.ServerHostGetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationServerLinkGeneratorImpl implements ApplicationServerLinkGenerator{

  @Value("${server.protocol}")
  private String protocol;
  @Value("${server.port}")
  private int port;

  @Override
  public String generate() {
    return protocol +  ServerHostGetter.getLocalIPv4() + ":" + port;
  }
}
