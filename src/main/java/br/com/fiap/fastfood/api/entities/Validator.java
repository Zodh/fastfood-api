package br.com.fiap.fastfood.api.entities;

import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import java.util.List;

public interface Validator<T> {

  List<ErrorDetail> validate(T object);

}
