package ee.swedbank.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuelConsumptionDto {

	private String fuelType;
	private BigDecimal volume;
	private BigDecimal pricePerLiter;
	private LocalDate consumptionDate;
	private Long driverId;
	private BigDecimal totalPrice;
	
	@Override
	public String toString() {
		return "FuelType: " + this.getFuelType() + " Volume: " + this.getVolume() +
				" PricePerLiter: " + this.getPricePerLiter() + " Date: " + this.getPricePerLiter() +
				" driver: " + this.getDriverId() + " ";
	}
}
