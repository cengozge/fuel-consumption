package ee.swedbank.application.workers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.swedbank.application.entity.Driver;
import ee.swedbank.application.entity.FuelConsumption;
import ee.swedbank.application.enums.FuelType;

@Service
@Transactional
public class DataGenerator {

	@Autowired
	private EntityManager entityManager;

	private static final Random RANDOM = new Random();

	public void generateData() {

		final int DRIVER_SIZE = 100;
		final int CONSUMPTION_SIZE = 10000;

		// generate driver data
		for (int id = 1; id <= DRIVER_SIZE; id++) {
			Driver driver = Driver
					.builder()
					.name(RandomStringUtils.random(5, "abcdefghijklmnopqrstuvwxyz"))
					.build();
		
			entityManager.persist(driver);
		}
		
		// generate consumption data
		LocalDate date = LocalDate.now();

		for (int id = 1; id < CONSUMPTION_SIZE; id++) {
			LocalDate tempDate = date;
			if(id != 1) {
				date = tempDate.plusDays(1);
			}
			for (int i = 0; i < 5; i++) {
				// generate Fuel_Price data (Math.random()*((max-min)+1))+min
				BigDecimal randomPricePerLiter = new BigDecimal((Math.random()*2)).setScale(2, RoundingMode.HALF_UP);
				BigDecimal randomVolume = new BigDecimal((Math.random()*101)).setScale(1, RoundingMode.HALF_UP);
				FuelType randomFuelType = FuelType.values()[RANDOM.nextInt(FuelType.values().length)];

				FuelConsumption fuelConsumption = FuelConsumption
						.builder()
						.fuelType(randomFuelType.name())
						.pricePerLiter(randomPricePerLiter)
						.volume(randomVolume)
						.driverId(new Long(RANDOM.nextInt(DRIVER_SIZE) + 1))
						.consumptionDate(date)
						.build();
				
				entityManager.persist(fuelConsumption);
			}
		}		

	}
}
