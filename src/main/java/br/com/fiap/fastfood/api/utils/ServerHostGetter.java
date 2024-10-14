package br.com.fiap.fastfood.api.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ServerHostGetter {

  public static String getLocalIPv4() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();

        // Ignora interfaces de loopback e interfaces que nao estao ativas
        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
          continue;
        }

        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress addr = addresses.nextElement();

          // Verifica se o endereco e IPv4
          if (addr.getHostAddress().indexOf(":") == -1) {
            return addr.getHostAddress();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null; // Retorna null se nao for encontrado nenhum IPv4
  }

}
