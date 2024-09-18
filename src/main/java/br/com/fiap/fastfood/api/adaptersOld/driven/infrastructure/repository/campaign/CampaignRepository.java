package br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.repository.campaign;

import br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.entity.campaign.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, Long> {

}
