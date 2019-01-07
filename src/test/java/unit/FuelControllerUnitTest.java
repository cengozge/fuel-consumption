package unit;

import static ee.swedbank.application.enums.FuelType.FUEL_95;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ee.swedbank.application.controllers.FuelConsumptionController;
import ee.swedbank.application.dtos.FuelConsumptionDto;
import ee.swedbank.application.service.FuelConsumptionService;

@RunWith(MockitoJUnitRunner.class)
public class FuelControllerUnitTest {
    
	@InjectMocks
    private FuelConsumptionController fuelConsumptionController;
	
	@Mock
	private FuelConsumptionService fuelConsumptionService;

	
	@Test
	public void saveFuelConsumption_success() throws Exception {
		FuelConsumptionDto fuelConsumptionDto = buildFuelConsumptionDto();

		when(fuelConsumptionService.saveFuelConsumption(any())).thenReturn(any());
		
		fuelConsumptionController.saveFuelConsumption(fuelConsumptionDto);
		verify(fuelConsumptionService).saveFuelConsumption(any());
	}

	private FuelConsumptionDto buildFuelConsumptionDto() {
		return FuelConsumptionDto
				.builder()
				.fuelType(FUEL_95.name())
				.volume(new BigDecimal("100.60"))
				.pricePerLiter(new BigDecimal("1.28"))
				.consumptionDate(LocalDate.now())
				.driverId(new Long("1"))
				.build();
	}
}
