package br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.followup.FollowUpEntity;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FollowUpMapperInfra {

  FollowUpDTO toDTO(FollowUpEntity entity);

  FollowUpEntity toEntity(FollowUpDTO dto);

}
