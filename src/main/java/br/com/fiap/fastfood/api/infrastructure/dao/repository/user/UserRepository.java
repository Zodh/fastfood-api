package br.com.fiap.fastfood.api.infrastructure.dao.repository.user;

import br.com.fiap.fastfood.api.infrastructure.dao.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
