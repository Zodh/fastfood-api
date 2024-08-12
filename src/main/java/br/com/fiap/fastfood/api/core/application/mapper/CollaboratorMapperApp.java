package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollaboratorMapperApp {

  Collaborator toDomain(CollaboratorDTO dto);

}
