package ee.swedbank.application.dao;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ee.swedbank.application.dtos.FuelConsumptionDto;
import ee.swedbank.application.dtos.FuelConsumptionStatDto;
import ee.swedbank.application.dtos.MonthBasedAmountDto;
import ee.swedbank.application.entity.FuelConsumption;

@Repository
public class FuelConsumptionDao extends BaseDao {

	@Autowired
	EntityManager entityManager;
	
	public void saveFuelConsumption(FuelConsumption fuelConsumption) {
		save(fuelConsumption);
	}

	public List<MonthBasedAmountDto> findMonthlySpentMoney() {
		return entityManager
				.createQuery("select EXTRACT(YEAR FROM consumptionDate) as year,"
						+ "EXTRACT(MONTH FROM consumptionDate) as month,"
						+ "sum(pricePerLiter * volume) as totalPrice from FuelConsumption f "
						+ "group by EXTRACT(YEAR FROM consumptionDate), EXTRACT(MONTH FROM consumptionDate) "
						+ "order by year, month")
				.unwrap(Query.class)
				.setResultTransformer(Transformers.aliasToBean(MonthBasedAmountDto.class))
				.getResultList();
	}
	
	public List<FuelConsumptionDto> findConsumptionDetails(int monthId, int year) {
		 return entityManager
				.createQuery("select fuelType as fuelType,"
						+ "volume as volume,"
						+ "pricePerLiter as pricePerLiter,"
						+ "(pricePerLiter * volume) as totalPrice,"
						+ "consumptionDate as consumptionDate,"
						+ "driverId as driverId "
						+ "from FuelConsumption "
						+ "where consumptionDate between :startDate and :endDate")
				.setParameter("startDate", LocalDate.of(year, monthId, 1))
				.setParameter("endDate", LocalDate.of(year, monthId, Month.of(monthId).maxLength()))
				.unwrap(Query.class)
				.setResultTransformer(Transformers.aliasToBean(FuelConsumptionDto.class))
				.getResultList();
	}
	
	public List<FuelConsumptionStatDto> findStatistics(int year) {
		return entityManager
				.createQuery("select EXTRACT(MONTH FROM consumptionDate) as month, fuelType as fuelType,"
						+ "sum(volume) as totalVolume,"
						+ "sum(volume * pricePerLiter) as totalPrice,"
						+ "(sum(volume * pricePerLiter)/sum(volume)) as averagePrice "
						+ "from FuelConsumption "
						+ "where EXTRACT(YEAR FROM consumptionDate) = :year "
						+ "group by EXTRACT(MONTH FROM consumptionDate), fuelType "
						+ "order by EXTRACT(MONTH FROM consumptionDate), fuelType")
				.setParameter("year", year)
				.unwrap(Query.class)
				.setResultTransformer(Transformers.aliasToBean(FuelConsumptionStatDto.class))
				.getResultList();
	}
	
	public List<MonthBasedAmountDto> findMonthlySpentMoneyByDriver(int driverId) {
		return entityManager
				.createQuery("select EXTRACT(YEAR FROM consumptionDate) as year,"
						+ "EXTRACT(MONTH FROM consumptionDate) as month,"
						+ "sum(pricePerLiter * volume) as totalPrice from FuelConsumption f "
						+ "where driverId = :driverId "
						+ "group by EXTRACT(YEAR FROM consumptionDate), EXTRACT(MONTH FROM consumptionDate) "
						+ "order by year, month")
				.setParameter("driverId", driverId)
				.unwrap(Query.class)
				.setResultTransformer(Transformers.aliasToBean(MonthBasedAmountDto.class))
				.getResultList();
	}
	
	public List<FuelConsumptionDto> findConsumptionDetailsByDriver(int monthId, int year, int driverId) {
		return entityManager
				.createQuery("select fuelType as fuelType,"
						+ "volume as volume,"
						+ "pricePerLiter as pricePerLiter,"
						+ "(pricePerLiter * volume) as totalPrice,"
						+ "consumptionDate as consumptionDate,"
						+ "driverId as driverId "
						+ "from FuelConsumption "
						+ "where consumptionDate between :startDate and :endDate "
						+ "and driverId = :driverId")
				.setParameter("startDate", LocalDate.of(year, monthId, 1))
				.setParameter("endDate", LocalDate.of(year, monthId, Month.of(monthId).maxLength()))
				.setParameter("driverId", driverId)
				.unwrap(Query.class)
				.setResultTransformer(Transformers.aliasToBean(FuelConsumptionDto.class))
				.getResultList();
	}
	
	public List<FuelConsumptionStatDto> findStatisticsByDriver(int year, int driverId) {
		return entityManager
				.createQuery("select EXTRACT(MONTH FROM consumptionDate) as month, fuelType as fuelType,"
						+ "sum(volume) as totalVolume,"
						+ "sum(volume * pricePerLiter) as totalPrice,"
						+ "(sum(volume * pricePerLiter)/sum(volume)) as averagePrice "
						+ "from FuelConsumption "
						+ "where EXTRACT(YEAR FROM consumptionDate) = :year "
						+ "and driverId = :driverId "
						+ "group by EXTRACT(MONTH FROM consumptionDate), fuelType "
						+ "order by EXTRACT(MONTH FROM consumptionDate), fuelType")
				.setParameter("year", year)
				.setParameter("driverId", driverId)
				.unwrap(Query.class)
				.setResultTransformer(Transformers.aliasToBean(FuelConsumptionStatDto.class))
				.getResultList();
	}
}
