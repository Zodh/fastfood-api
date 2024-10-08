package br.com.fiap.fastfood.api.infrastructure.dao.repository.product;

import br.com.fiap.fastfood.api.infrastructure.dao.entity.product.CampaignProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignProductRepository extends JpaRepository<CampaignProductEntity, Long> {

}
