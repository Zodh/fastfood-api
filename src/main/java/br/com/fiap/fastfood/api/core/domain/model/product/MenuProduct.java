package br.com.fiap.fastfood.api.core.domain.model.product;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MenuProduct extends Product{

    protected List<MenuProduct> optionals;
    protected List<MenuProduct> ingredients;
}
