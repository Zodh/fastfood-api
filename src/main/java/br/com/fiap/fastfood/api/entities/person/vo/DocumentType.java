package br.com.fiap.fastfood.api.entities.person.vo;

import lombok.Getter;

@Getter
public enum DocumentType {
  CPF("([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})");

  private final String regex;

  DocumentType(String regex) {
    this.regex = regex;
  }

}
