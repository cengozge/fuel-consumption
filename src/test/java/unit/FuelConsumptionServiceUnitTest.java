package unit;
import static ee.swedbank.application.constant.Constants.CONSUMPTION_NOT_SAVED;
import static ee.swedbank.application.constant.Constants.DRIVER_NOT_FOUND;
import static ee.swedbank.application.enums.FuelType.FUEL_95;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import ee.swedbank.application.dao.FuelConsumptionDao;
import ee.swedbank.application.dtos.FuelConsumptionDto;
import ee.swedbank.application.dtos.FuelConsumptionStatDto;
import ee.swedbank.application.dtos.MonthBasedAmountDto;
import ee.swedbank.application.entity.Driver;
import ee.swedbank.application.entity.FuelConsumption;
import ee.swedbank.application.service.DriverService;
import ee.swedbank.application.service.FuelConsumptionService;
import ee.swedbank.application.service.NewTransactionService;

@RunWith(MockitoJUnitRunner.class)
public class FuelConsumptionServiceUnitTest {

	@InjectMocks
	private FuelConsumptionService fuelConsumptionService;

	@Mock
	private FuelConsumptionDao fuelConsumptionDao;

	@Mock
	private DriverService driverService;

	@Mock
	private ModelMapper modelMappper;

	@Mock
	private NewTransactionService newTransactionService;

	@Test
	public void saveFuelConsumptionList_shouldDoNothingWithNullList() {
		List<FuelConsumptionDto> dtoList = new ArrayList<>();
		List<String> messageList = fuelConsumptionService.saveFuelConsumptionList(dtoList);

		assertThat(messageList).isEmpty();
		verifyZeroInteractions(fuelConsumptionDao);
		verifyZeroInteractions(newTransactionService);
	}

	@Test
	public void saveFuelConsumptionList_shouldSaveSuccessWithNotNullAndValidList() {
		FuelConsumptionDto fuelConsumptionDtoValidDriver = buildFuelConsumptionDto();

		List<FuelConsumptionDto> dtoList = new ArrayList<>();
		dtoList.add(fuelConsumptionDtoValidDriver);

		doNothing().when(newTransactionService).saveFuelConsumptionWithNewTransation(fuelConsumptionDtoValidDriver);

		List<String> messageList = fuelConsumptionService.saveFuelConsumptionList(dtoList);

		assertThat(messageList).isEmpty();
		verifyZeroInteractions(fuelConsumptionDao);
		verify(newTransactionService).saveFuelConsumptionWithNewTransation(fuelConsumptionDtoValidDriver);
	}

	@Test
	public void saveFuelConsumptionList_shouldThrowExWithInvalidList() throws Exception{
		FuelConsumptionDto fuelConsumptionDtoInvalidDriver = buildFuelConsumptionDto();

		List<String> messageList = new ArrayList<>();
		List<FuelConsumptionDto> dtoList = new ArrayList<>();
		dtoList.add(fuelConsumptionDtoInvalidDriver);

		doThrow(new RuntimeException(DRIVER_NOT_FOUND)).when(newTransactionService).saveFuelConsumptionWithNewTransation(fuelConsumptionDtoInvalidDriver);

		messageList = fuelConsumptionService.saveFuelConsumptionList(dtoList);
		
		assertEquals(1, messageList.size());
		assertEquals(fuelConsumptionDtoInvalidDriver.toString().concat(CONSUMPTION_NOT_SAVED) + "Reason: " + DRIVER_NOT_FOUND, messageList.get(0));
		verifyZeroInteractions(fuelConsumptionDao);
		verify(newTransactionService).saveFuelConsumptionWithNewTransation(fuelConsumptionDtoInvalidDriver);
	}
	

	@Test
	public void saveFuelConsumption_shouldSaveSuccessWithValidDto() {
		FuelConsumption fuelConsumption = new FuelConsumption();
		FuelConsumptionDto fuelConsumptionDtoValidDriver = buildFuelConsumptionDto();
		
		Driver driver = Driver
				.builder()
				.name("test")
				.build();
		
		when(modelMappper.map(fuelConsumptionDtoValidDriver, FuelConsumption.class)).thenReturn(fuelConsumption);
		when(driverService.getDriverById(new Long("1"))).thenReturn(driver);
		doNothing().when(fuelConsumptionDao).saveFuelConsumption(fuelConsumption);
		
		fuelConsumptionService.saveFuelConsumption(fuelConsumptionDtoValidDriver);
		
    	verify(fuelConsumptionDao).saveFuelConsumption(fuelConsumption);
    	verify(driverService).getDriverById(new Long("1"));
    	verifyNoMoreInteractions(fuelConsumptionDao);
    	verifyNoMoreInteractions(driverService);
	}
	
	@Test
	public void saveFuelConsumption_shouldThrowExWithInvalidDto() {
		FuelConsumption fuelConsumption = new FuelConsumption();
		FuelConsumptionDto fuelConsumptionDtoValidDriver = new FuelConsumptionDto();
		
		when(modelMappper.map(fuelConsumptionDtoValidDriver, FuelConsumption.class)).thenReturn(fuelConsumption);
		when(driverService.getDriverById(any())).thenReturn(null);
		
		try {
			fuelConsumptionService.saveFuelConsumption(fuelConsumptionDtoValidDriver);
		} catch (Exception e) {
			assertEquals(DRIVER_NOT_FOUND, e.getMessage());
		}
		
		verify(driverService).getDriverById(any());
		verifyZeroInteractions(fuelConsumptionDao);
	}
	
	@Test
	public void getMonthlySpentMoney_success() {
		List<MonthBasedAmountDto> expectedMonthlySpent = new ArrayList<>();
		when(fuelConsumptionDao.findMonthlySpentMoney()).thenReturn(expectedMonthlySpent);
		
		List<MonthBasedAmountDto> actualMonthlySpent = fuelConsumptionService.getMonthlySpentMoney();
		
		assertEquals(expectedMonthlySpent, actualMonthlySpent);
		verify(fuelConsumptionDao).findMonthlySpentMoney();
		verifyNoMoreInteractions(fuelConsumptionDao);
		verifyZeroInteractions(driverService);
	}
	
	
	@Test
	public void getMonthlySpentMoneyByDriver_success() {
		List<MonthBasedAmountDto> expectedMonthlySpent = new ArrayList<>();
		when(fuelConsumptionDao.findMonthlySpentMoneyByDriver(1)).thenReturn(expectedMonthlySpent);
		
		List<MonthBasedAmountDto> actualMonthlySpent = fuelConsumptionService.getMonthlySpentMoneyByDriver(1);
		
		assertEquals(expectedMonthlySpent, actualMonthlySpent);
		verify(fuelConsumptionDao).findMonthlySpentMoneyByDriver(1);
		verifyNoMoreInteractions(fuelConsumptionDao);
		verifyZeroInteractions(driverService);
	}
	
	@Test
	public void getConsumptionDetailsByMonth_success() {
		List<FuelConsumptionDto> expectedDetails = new ArrayList<>();
		when(fuelConsumptionDao.findConsumptionDetails(1, 2019)).thenReturn(expectedDetails);
		
		List<FuelConsumptionDto> actualDetails = fuelConsumptionService.getConsumptionDetails(1, 2019);
		
		assertEquals(expectedDetails, actualDetails);
		verify(fuelConsumptionDao).findConsumptionDetails(1, 2019);
		verifyNoMoreInteractions(fuelConsumptionDao);
		verifyZeroInteractions(driverService);
	}
	
	@Test
	public void getConsumptionDetailsByDriver_success() {
		List<FuelConsumptionDto> expectedDetails = new ArrayList<>();
		when(fuelConsumptionDao.findConsumptionDetailsByDriver(1, 2019, 1)).thenReturn(expectedDetails);
		
		List<FuelConsumptionDto> actualDetails = fuelConsumptionService.getConsumptionDetailsByDriver(1, 2019, 1);
		
		assertEquals(expectedDetails, actualDetails);
		verify(fuelConsumptionDao).findConsumptionDetailsByDriver(1, 2019, 1);
		verifyNoMoreInteractions(fuelConsumptionDao);
		verifyZeroInteractions(driverService);
	}
	
	@Test
	public void getStatistics_success() {
		List<FuelConsumptionStatDto> expectedStatistics = new ArrayList<>();
		when(fuelConsumptionDao.findStatistics(1)).thenReturn(expectedStatistics);
		
		List<FuelConsumptionStatDto> actualStatistics = fuelConsumptionService.getStatistics(1);
		assertEquals(expectedStatistics, actualStatistics);
		verify(fuelConsumptionDao).findStatistics(1);
		verifyNoMoreInteractions(fuelConsumptionDao);
		verifyZeroInteractions(driverService);
	}
	
	@Test
	public void getStatisticsByDriver_success() {
		List<FuelConsumptionStatDto> expectedStatistics = new ArrayList<>();
		when(fuelConsumptionDao.findStatisticsByDriver(1, 1)).thenReturn(expectedStatistics);
		
		List<FuelConsumptionStatDto> actualStatistics = fuelConsumptionService.getStatisticsByDriver(1, 1);
		assertEquals(expectedStatistics, actualStatistics);
		verify(fuelConsumptionDao).findStatisticsByDriver(1, 1);
		verifyNoMoreInteractions(fuelConsumptionDao);
		verifyZeroInteractions(driverService);
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
