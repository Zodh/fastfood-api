package br.com.fiap.fastfood.api.core.domain.repository.outbound;

import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import br.com.fiap.fastfood.api.core.domain.repository.BaseRepository;
import java.util.UUID;

public interface ActivationCodeRepositoryPort extends BaseRepository<ActivationCode, UUID> {

}
