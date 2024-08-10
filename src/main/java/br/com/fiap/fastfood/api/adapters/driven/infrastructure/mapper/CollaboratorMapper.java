package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CollaboratorEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.DocumentTypeEnum;
import br.com.fiap.fastfood.api.core.application.dto.collaborator.CollaboratorDTO;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.DocumentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollaboratorMapper {

  @Mapping(source = "collaborator.document.value", target = "documentNumber")
  @Mapping(source = "collaborator.document.type", target = "documentType")
  @Mapping(source = "collaborator.email.value", target = "email")
  @Mapping(source = "collaborator.phoneNumber.value", target = "phoneNumber")
  CollaboratorEntity toEntity(Collaborator collaborator);

  @Mapping(source = "entity.documentNumber", target = "document.value")
  @Mapping(source = "entity.documentType", target = "document.type", qualifiedByName = "mapDocumentType")
  @Mapping(source = "entity.email", target = "email.value")
  @Mapping(source = "entity.phoneNumber", target = "phoneNumber.value")
  Collaborator toDomain(CollaboratorEntity entity);

  Collaborator toDomain(CollaboratorDTO dto);

  @Named("mapDocumentType")
  default DocumentType toDocumentType(DocumentTypeEnum documentType) {
    return DocumentType.valueOf(documentType.name());
  }

}
