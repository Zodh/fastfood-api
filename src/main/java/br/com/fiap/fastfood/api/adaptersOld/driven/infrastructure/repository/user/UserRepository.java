package br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.repository.user;

import br.com.fiap.fastfood.api.adaptersOld.driven.infrastructure.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
