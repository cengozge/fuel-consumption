package unit;

import static ee.swedbank.application.enums.FuelType.FUEL_95;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static ee.swedbank.application.constant.Constants.DRIVER_NOT_FOUND;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import ee.swedbank.application.dao.FuelConsumptionDao;
import ee.swedbank.application.dtos.FuelConsumptionDto;
import ee.swedbank.application.entity.Driver;
import ee.swedbank.application.entity.FuelConsumption;
import ee.swedbank.application.service.DriverService;
import ee.swedbank.application.service.NewTransactionService;

@RunWith(MockitoJUnitRunner.class)
public class NewTransactionServiceUnitTest {

	@InjectMocks
	private NewTransactionService newTransactionService;

	@Mock
	private FuelConsumptionDao fuelConsumptionDao;

	@Mock
	private ModelMapper modelMappper;

	@Mock
	private DriverService driverService;
	
	@Test
	public void saveFuelConsumptionWithNewTransation_shouldSaveSuccessWithValidDto() {
		FuelConsumption fuelConsumption = new FuelConsumption();
		FuelConsumptionDto fuelConsumptionDtoValidDriver = FuelConsumptionDto
				.builder()
				.fuelType(FUEL_95.name())
				.volume(new BigDecimal("100.60"))
				.pricePerLiter(new BigDecimal("1.28"))
				.consumptionDate(LocalDate.now())
				.driverId(new Long("1"))
				.build();
		
		Driver driver = Driver
				.builder()
				.name("test")
				.build();
		
		when(modelMappper.map(fuelConsumptionDtoValidDriver, FuelConsumption.class)).thenReturn(fuelConsumption);
		when(driverService.getDriverById(new Long("1"))).thenReturn(driver);
		doNothing().when(fuelConsumptionDao).saveFuelConsumption(fuelConsumption);
		
		newTransactionService.saveFuelConsumptionWithNewTransation(fuelConsumptionDtoValidDriver);
		
    	verify(fuelConsumptionDao).saveFuelConsumption(fuelConsumption);
    	verify(driverService).getDriverById(new Long("1"));
    	verifyNoMoreInteractions(fuelConsumptionDao);
    	verifyNoMoreInteractions(driverService);
	}
	
	@Test
	public void saveFuelConsumptionWithNewTransation_shouldThrowExWithInvalidDto() {
		FuelConsumption fuelConsumption = new FuelConsumption();
		FuelConsumptionDto fuelConsumptionDtoValidDriver = new FuelConsumptionDto();
		
		when(modelMappper.map(fuelConsumptionDtoValidDriver, FuelConsumption.class)).thenReturn(fuelConsumption);
		when(driverService.getDriverById(any())).thenReturn(null);
		
		try {
			newTransactionService.saveFuelConsumptionWithNewTransation(fuelConsumptionDtoValidDriver);
		} catch (Exception e) {
			assertEquals(DRIVER_NOT_FOUND, e.getMessage());
		}
		
		verify(driverService).getDriverById(any());
		verifyZeroInteractions(fuelConsumptionDao);
		verifyNoMoreInteractions(fuelConsumptionDao);
    	verifyNoMoreInteractions(driverService);
	}

}
