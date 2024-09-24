package br.com.fiap.fastfood.api.infrastructure.dao.repository.follow.up;

import br.com.fiap.fastfood.api.infrastructure.dao.entity.followup.FollowUpEntity;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.followup.FollowUpStateEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUpEntity, Long> {

  List<FollowUpEntity> findAllByState(FollowUpStateEnum state);
  Optional<FollowUpEntity> findByOrder_Id(Long orderId);

  @Query("SELECT new br.com.fiap.fastfood.api.infrastructure.dao.entity.followup.FollowUpEntity(fu.id, fu.showOrder, fu.order.id, fu.state) "
      + "FROM FollowUpEntity fu")
  List<FollowUpEntity> findAllLazy();

}
