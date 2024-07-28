package br.com.fiap.fastfood.api.core.domain.model.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductValidatorTest {

  private final ProductValidator productValidator = new ProductValidator();

  @Test
  @DisplayName("Given null product "
      + "When validate "
      + "Then throws exception ")
  void throwExceptionWhenProductIsNull() {
    assertThrows(DomainException.class, () -> productValidator.validate(null));
  }

  @Test
  @DisplayName("Given Product With 2 Chars "
      + "When Validate "
      + "Then Add Name Error")
  void tinyNameError() {
    MenuProduct product = ProductMock.mockValidMenuProduct();
    product.setName("Ta");
    List<ErrorDetail> result = productValidator.validate(product);
    assertThat(result).isNotEmpty();
    assertThat(result.getFirst()).isEqualTo(new ErrorDetail("product.name", "O nome do produto não pode ser nulo e deve conter entre 3 e 50 caracteres."));
  }

  @Test
  @DisplayName("Given Product With 51 Chars "
      + "When Validate "
      + "Then Add Name Error")
  void bigNameError() {
    MenuProduct product = ProductMock.mockValidMenuProduct();
    product.setName("Ta".repeat(30));
    List<ErrorDetail> result = productValidator.validate(product);
    assertThat(result).isNotEmpty();
    assertThat(result.getFirst()).isEqualTo(new ErrorDetail("product.name", "O nome do produto não pode ser nulo e deve conter entre 3 e 50 caracteres."));
  }

  @Test
  @DisplayName("Given product without price and not ingredient and not optional "
      + "When Validate"
      + "Then add product price error")
  void invalidPriceError() {
    MenuProduct product = ProductMock.mockValidMenuProduct();
    product.setPrice(BigDecimal.ZERO);
    List<ErrorDetail> result = productValidator.validate(product);
    assertThat(result).isNotEmpty();
    assertThat(result.getFirst()).isEqualTo(new ErrorDetail("product.price", "O preço do produto não pode ser igual ou menor do que zero."));
  }

  @Test
  @DisplayName("Given product without preparation time and not ingredient and not optional "
      + "When Validate"
      + "Then add product price error")
  void invalidPreparationTimeError() {
    MenuProduct product = ProductMock.mockValidMenuProduct();
    product.setPreparationTimeInMillis(0L);
    List<ErrorDetail> result = productValidator.validate(product);
    assertThat(result).isNotEmpty();
    assertThat(result.getFirst()).isEqualTo(new ErrorDetail("product.preparationTime", "O tempo de preparo do produto não pode ser igual ou menor do que zero."));
  }

  @Test
  @DisplayName("Given product not optional and without quantity "
      + "When Validate "
      + "Then Add product quantity error")
  void invalidProductQuantityError() {
    MenuProduct product = ProductMock.mockValidMenuProduct();
    product.setQuantity(0);
    List<ErrorDetail> result = productValidator.validate(product);
    assertThat(result).isNotEmpty();
    assertThat(result.getFirst()).isEqualTo(new ErrorDetail("product.quantity", "A quantidade deve ser informada!"));
  }

  @Test
  @DisplayName("Given Product Ingredient When Has Not Cost "
      + "When Validate "
      + "Then Add Cost Error")
  void invalidCostError() {
    MenuProduct ketchup = new MenuProduct();
    ketchup.setName("Ketchup");
    ketchup.setQuantity(1);
    ketchup.setIngredient(true);
    List<ErrorDetail> result = productValidator.validate(ketchup);
    assertThat(result).isNotEmpty();
    assertThat(result.getFirst()).isEqualTo(new ErrorDetail("product.cost", "O custo do ingrediente deve ser informado!"));
  }

  @Test
  @DisplayName("Given Valid Product "
      + "When Validate "
      + "Then No Errors")
  void validProductTest() {
    MenuProduct product = ProductMock.mockValidMenuProduct();
    List<ErrorDetail> result = productValidator.validate(product);
    assertThat(result).isEmpty();
  }

}
