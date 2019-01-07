package ee.swedbank.application.dao;

import org.springframework.stereotype.Repository;

import ee.swedbank.application.entity.Driver;

@Repository
public class DriverDao extends BaseDao {
	
	public Driver getDriverById(Long driverId) {
		return getSession().find(Driver.class, driverId);
	}
	
	public void saveDriver(Driver driver) {
		save(driver);
	}
	
}
