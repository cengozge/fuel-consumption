package ee.swedbank.application.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ee.swedbank.application.constant.Constants.DRIVER_SAVED;

import ee.swedbank.application.dao.DriverDao;
import ee.swedbank.application.dtos.DriverDto;
import ee.swedbank.application.entity.Driver;

@Service
@Transactional
public class DriverService {

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private ModelMapper modelMappper;
    
    public Driver getDriverById(Long driverId) {
    	return driverDao.getDriverById(driverId);
    }
    
    public String saveDriver(DriverDto driverDto) {
    	Driver driver = modelMappper.map(driverDto, Driver.class);
    	driverDao.saveDriver(driver);
    	return driverDto.toString() + DRIVER_SAVED;
    }
    
}
