package br.com.fiap.fastfood.api.core.domain.model.person.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
public class PhoneNumber {

  private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
      "^\\([1-9]{2}\\) (?:[2-8]|9\\d)\\d{3}-\\d{4}$|^\\([1-9]{2}\\) (?:[2-8]|\\d)\\d{3}-\\d{4}$"
  );
  private String value;

  public boolean isValid() {
    Matcher matcher = PHONE_NUMBER_PATTERN.matcher(value);
    return StringUtils.hasText(value) && matcher.find();
  }

}
