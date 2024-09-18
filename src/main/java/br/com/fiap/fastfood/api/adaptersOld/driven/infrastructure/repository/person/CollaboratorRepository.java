package br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.repository.person;

import br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.entity.person.CollaboratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends JpaRepository<CollaboratorEntity, Long> {

}
