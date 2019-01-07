package ee.swedbank.application.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthBasedAmountDto {

	private int year;
	private int month;
	private BigDecimal totalPrice;
	
}
