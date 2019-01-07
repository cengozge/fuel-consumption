package ee.swedbank.application.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ee.swedbank.application.constant.Constants.CONSUMPTION_SAVED;
import static ee.swedbank.application.constant.Constants.CONSUMPTION_NOT_SAVED;
import static ee.swedbank.application.constant.Constants.DRIVER_NOT_FOUND;
import ee.swedbank.application.dao.FuelConsumptionDao;
import ee.swedbank.application.dtos.FuelConsumptionDto;
import ee.swedbank.application.dtos.FuelConsumptionStatDto;
import ee.swedbank.application.dtos.MonthBasedAmountDto;
import ee.swedbank.application.entity.FuelConsumption;

@Service
@Transactional
public class FuelConsumptionService {

	@Autowired
	private FuelConsumptionDao fuelConsumptionDao;

	@Autowired
	private DriverService driverService;

	@Autowired
	private ModelMapper modelMappper;

	@Autowired
	private NewTransactionService newTransactionService;

	public List<String> saveFuelConsumptionList(List<FuelConsumptionDto> fuelConsumptionDtoList) {
		List<String> messageList = new ArrayList<>();
		fuelConsumptionDtoList.forEach(dto -> {
			try {
				newTransactionService.saveFuelConsumptionWithNewTransation(dto);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				messageList.add(dto.toString().concat(CONSUMPTION_NOT_SAVED).concat("Reason: " + e.getMessage()));
			}
		});
		return messageList;
	}

	public String saveFuelConsumption(FuelConsumptionDto fuelConsumptionDto) {
		FuelConsumption fuelConsumption = modelMappper.map(fuelConsumptionDto, FuelConsumption.class);
		if (driverService.getDriverById(fuelConsumptionDto.getDriverId()) == null) {
			throw new RuntimeException(DRIVER_NOT_FOUND);
		}
		fuelConsumptionDao.saveFuelConsumption(fuelConsumption);
		return fuelConsumptionDto.toString().concat(CONSUMPTION_SAVED);
	}

	public List<MonthBasedAmountDto> getMonthlySpentMoney() {
		return fuelConsumptionDao.findMonthlySpentMoney();
	}

	public List<FuelConsumptionDto> getConsumptionDetails(int monthId, int year) {
		return fuelConsumptionDao.findConsumptionDetails(monthId, year);
	}

	public List<FuelConsumptionStatDto> getStatistics(int year) {
		return fuelConsumptionDao.findStatistics(year);
	}

	public List<MonthBasedAmountDto> getMonthlySpentMoneyByDriver(int driverId) {
		return fuelConsumptionDao.findMonthlySpentMoneyByDriver(driverId);
	}

	public List<FuelConsumptionDto> getConsumptionDetailsByDriver(int monthId, int year, int driverId) {
		return fuelConsumptionDao.findConsumptionDetailsByDriver(monthId, year, driverId);
	}

	public List<FuelConsumptionStatDto> getStatisticsByDriver(int year, int driverId) {
		return fuelConsumptionDao.findStatisticsByDriver(year, driverId);
	}

}
