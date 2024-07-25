package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.activation;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.activation.ActivationCodeEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCodeEntity, UUID> {

}
