package br.com.fiap.fastfood.api.adapters.mapper;

import br.com.fiap.fastfood.api.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.followup.FollowUpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FollowUpMapperInfra {

  FollowUpDTO toDTO(FollowUpEntity entity);

  FollowUpEntity toEntity(FollowUpDTO dto);

}
