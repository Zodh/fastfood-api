package br.com.fiap.fastfood.api.entities;

import br.com.fiap.fastfood.api.entities.exception.ErrorDetail;
import java.util.List;

public interface Validator<T> {

  List<ErrorDetail> validate(T object);

}
