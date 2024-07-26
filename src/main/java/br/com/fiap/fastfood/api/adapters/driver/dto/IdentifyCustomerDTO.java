package br.com.fiap.fastfood.api.adapters.driver.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class IdentifyCustomerDTO {

    private Long id;
    private String name;
    private String documentNumber;
}
