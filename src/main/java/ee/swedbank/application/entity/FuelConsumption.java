package ee.swedbank.application.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "T_FUEL_CONSUMPTION")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)	
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuelConsumption extends BaseEntity {

	private String fuelType;
	private BigDecimal volume;
	private BigDecimal pricePerLiter;
	private LocalDate consumptionDate;
	private Long driverId;

}
