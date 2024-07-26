package br.com.fiap.fastfood.api.adapters.driver.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class MenuProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long preparationTime;
    private int quantity;
    private BigDecimal cost;
    private List<MenuProductDTO> optionals;
    private List<MenuProductDTO> ingredients;
}