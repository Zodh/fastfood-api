package br.com.fiap.fastfood.api.adapters.driver.dto.customer;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class CustomerIdentityDTO {

    private Long id;
    private String name;
    private String documentNumber;
}
