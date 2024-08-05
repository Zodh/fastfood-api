package br.com.fiap.fastfood.api.core.domain.exception;

import lombok.Getter;

@Getter
public class InvoiceStateException extends RuntimeException {

    public InvoiceStateException(String message) {
        super(message);
    }
}
