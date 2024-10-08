package br.com.fiap.fastfood.api.entities.person;

import br.com.fiap.fastfood.api.entities.exception.DomainException;
import br.com.fiap.fastfood.api.entities.exception.ErrorDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static java.util.Objects.isNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class Customer extends Person {

    public void checkDocument() {
        if (!this.getDocument().isValid()) {
            ErrorDetail errorDetail = new ErrorDetail("document", "O documento não é válido!");
            throw new DomainException(errorDetail);
        }
    }

    public void canResendActivationCode() {
        if (isNull(this) || this.isActive()) {
            throw new DomainException(new ErrorDetail("customer.active",
                    "Não é possível solicitar um código de ativação para um cliente ativo!"));
        }
    }
}
