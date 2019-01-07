package ee.swedbank.application.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static ee.swedbank.application.constant.Constants.DRIVER_NOT_FOUND;

import ee.swedbank.application.dao.FuelConsumptionDao;
import ee.swedbank.application.dtos.FuelConsumptionDto;
import ee.swedbank.application.entity.FuelConsumption;

@Service
public class NewTransactionService {

	@Autowired
	private DriverService driverService;

	@Autowired
	private ModelMapper modelMappper;

	@Autowired
	private FuelConsumptionDao fuelConsumptionDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFuelConsumptionWithNewTransation(FuelConsumptionDto fuelConsumptionDto) {
		FuelConsumption fuelConsumption = modelMappper.map(fuelConsumptionDto, FuelConsumption.class);
		if(driverService.getDriverById(fuelConsumptionDto.getDriverId()) == null) {
			throw new RuntimeException(DRIVER_NOT_FOUND);
		}
		fuelConsumptionDao.saveFuelConsumption(fuelConsumption);
	}
}
