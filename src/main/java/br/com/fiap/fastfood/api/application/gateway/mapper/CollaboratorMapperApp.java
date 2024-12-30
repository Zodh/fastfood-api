package br.com.fiap.fastfood.api.application.gateway.mapper;

import br.com.fiap.fastfood.api.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.entities.person.Collaborator;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollaboratorMapperApp {

  Collaborator toDomain(CollaboratorDTO dto);

}
