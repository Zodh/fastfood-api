package br.com.fiap.fastfood.api.core.application.dto.followup;

import java.util.List;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class FollowUpResponseDTO {

  private FollowUpStateEnum state;
  private List<FollowUpDTO> items;

}
