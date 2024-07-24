package br.com.fiap.fastfood.api.core.domain.model.person.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class Document {

  private final String value;
  private final DocumentType type;
  private final Matcher matcher;

  public Document(String value) {
    this.value = value;
    this.type = DocumentType.CPF;
    this.matcher = Pattern.compile(type.getRegex()).matcher(value);
  }

  public boolean isValid() {
    return StringUtils.hasText(this.value) && type != null && matcher.find();
  }

}
