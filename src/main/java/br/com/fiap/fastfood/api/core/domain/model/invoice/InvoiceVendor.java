package br.com.fiap.fastfood.api.core.domain.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceVendor {

    private Long id;

    private String name;
}
