package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.followup.FollowUp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FollowUpAggregate {

  private FollowUp root;

  public FollowUpAggregate(FollowUp root) {
    if (Objects.isNull(root)) {
      throw new DomainException(new ErrorDetail("followUp", "O acompanhamento não pode ser nulo!"));
    }
    this.root = root;
  }

  public void updateFollowUp(List<FollowUp> allFollowUpInPreviousState, List<FollowUp> allFollowUpInSameState) {
    removeFollowUp(allFollowUpInPreviousState, this.root);
    addFollowUp(allFollowUpInSameState, this.root);
  }

  public void removeFollowUp(List<FollowUp> list, FollowUp followUp) {
    list.removeIf(fu -> fu.getId().equals(followUp.getId()));
    if (!list.isEmpty()) {
      reorganizeShowOrder(list);
    }
  }

  private static void addFollowUp(List<FollowUp> list, FollowUp followUp) {
    int maxShowOrder = Optional.ofNullable(list).orElse(Collections.emptyList()).stream()
        .max(Comparator.comparingInt(FollowUp::getShowOrder))
        .map(FollowUp::getShowOrder)
        .orElse(0);
    followUp.setShowOrder(maxShowOrder + 1);
    if (Objects.isNull(list)) {
      list = new ArrayList<>();
    }
    list.add(followUp);
  }

  private static void reorganizeShowOrder(List<FollowUp> list) {
    list.sort(Comparator.comparingInt(FollowUp::getShowOrder));
    for (int i = 0; i < list.size(); i++) {
      list.get(i).setShowOrder(i + 1);
    }
  }

}
