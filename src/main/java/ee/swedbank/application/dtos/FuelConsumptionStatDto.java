package ee.swedbank.application.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuelConsumptionStatDto {

	private String fuelType;
	private BigDecimal totalPrice;
	private BigDecimal averagePrice;
	private BigDecimal totalVolume;
	private int month;
	
}
