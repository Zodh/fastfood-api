package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.campaign.CampaignEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campaign_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignProductEntity extends MenuProductEntity {

  @ManyToOne
  private CampaignEntity campaign;

}
