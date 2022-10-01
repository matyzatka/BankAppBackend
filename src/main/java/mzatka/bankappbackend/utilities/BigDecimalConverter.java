package mzatka.bankappbackend.utilities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Converter
public class BigDecimalConverter implements AttributeConverter<BigDecimal, Long> {

  @Override
  public Long convertToDatabaseColumn(BigDecimal value) {
    if (value == null) {
      return null;
    } else {
      return value.multiply(BigDecimal.valueOf(100)).longValue();
    }
  }

  @Override
  public BigDecimal convertToEntityAttribute(Long value) {
    if (value == null) {
      return null;
    } else {
      return new BigDecimal(value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
    }
  }
}
