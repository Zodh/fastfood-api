package br.com.fiap.fastfood.api.adapters.mapper;

import br.com.fiap.fastfood.api.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.person.CollaboratorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollaboratorMapperInfra {

  CollaboratorEntity toEntity(CollaboratorDTO dto);

  CollaboratorDTO toDTO(CollaboratorEntity entity);

}
