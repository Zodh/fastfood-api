package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CollaboratorEntity;
import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollaboratorMapperInfra {

  CollaboratorEntity toEntity(CollaboratorDTO dto);

  CollaboratorDTO toDTO(CollaboratorEntity entity);

}
