package br.com.fiap.fastfood.api.core.domain.entity;

public class Customer extends Person {

  private String documentNumber;

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }
}
