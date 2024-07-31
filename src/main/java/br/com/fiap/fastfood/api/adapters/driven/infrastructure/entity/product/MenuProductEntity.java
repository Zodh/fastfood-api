package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "menu_product")
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuProductEntity extends ProductEntity {

    @ManyToMany
    @JoinTable(
            name = "menu_product_ingredient",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    protected List<MenuProductEntity> ingredients;

}
