package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "menu_product")
@Data
public class MenuProductEntity extends ProductEntity {

    @ManyToMany
    @JoinTable(
            name = "menu_product_optional",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "optional_id")
    )
    protected List<MenuProductEntity> optionals;

    @ManyToMany
    @JoinTable(
            name = "menu_product_ingredient",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    protected List<MenuProductEntity> ingredients;

}
