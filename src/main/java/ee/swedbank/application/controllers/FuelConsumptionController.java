package ee.swedbank.application.controllers;

import static ee.swedbank.application.constant.Endpoint.DETAILS;
import static ee.swedbank.application.constant.Endpoint.DRIVER;
import static ee.swedbank.application.constant.Endpoint.FUEL_CONSUMPTION;
import static ee.swedbank.application.constant.Endpoint.GENERATE_DATA;
import static ee.swedbank.application.constant.Endpoint.MONTHLY_SPENT_MONEY;
import static ee.swedbank.application.constant.Endpoint.ROOT;
import static ee.swedbank.application.constant.Endpoint.STATISTICS;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ee.swedbank.application.dtos.DriverDto;
import ee.swedbank.application.dtos.FuelConsumptionDto;
import ee.swedbank.application.dtos.FuelConsumptionStatDto;
import ee.swedbank.application.dtos.MonthBasedAmountDto;
import ee.swedbank.application.service.DriverService;
import ee.swedbank.application.service.FuelConsumptionService;
import ee.swedbank.application.workers.DataGenerator;
import ee.swedbank.application.workers.FileProcessor;

@RestController
@RequestMapping(ROOT)
public class FuelConsumptionController {

	@Autowired
	private FuelConsumptionService fuelConsumptionService;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private DataGenerator dataGenerator;
	
	@Autowired
	private FileProcessor fileProcessor;
	
	@PostMapping
	public List<String> saveBulkFuelConsumption(@RequestParam("file") MultipartFile file) {
		List<String> messageList = new ArrayList<>();
		try {
			messageList = fuelConsumptionService.saveFuelConsumptionList(fileProcessor.getFuelConsumptionListFromFile(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messageList;
	}
	
	@PostMapping(FUEL_CONSUMPTION)
	public String saveFuelConsumption(@RequestBody @Valid FuelConsumptionDto fuelDto) {
		return fuelConsumptionService.saveFuelConsumption(fuelDto);
	}
	
	@PostMapping(DRIVER)
	public String saveDriver(@RequestBody @Valid DriverDto driverDto) {
		return driverService.saveDriver(driverDto);
	}
	
	@GetMapping(MONTHLY_SPENT_MONEY)
	public List<MonthBasedAmountDto> getMonthlySpentMoney() {
		return fuelConsumptionService.getMonthlySpentMoney();
	}
	
	@GetMapping(DETAILS + "/{monthId}/{year}")
	public List<FuelConsumptionDto> getConsumptionDetailsByMonth(@PathVariable int monthId, @PathVariable int year) {
		return fuelConsumptionService.getConsumptionDetails(monthId, year);
	}
	
	@GetMapping(STATISTICS + "/{year}")
	public List<FuelConsumptionStatDto> getStatistics(@PathVariable int year) {
		return fuelConsumptionService.getStatistics(year)
				.stream()
				.map(row -> {
					row.setAveragePrice(row.getAveragePrice().setScale(3, RoundingMode.HALF_UP));
					return row; 	
				})
				.collect(Collectors.toList());
	}
	
	@GetMapping(MONTHLY_SPENT_MONEY + DRIVER + "/{driverId}")
	public List<MonthBasedAmountDto> getMonthlySpentMoneyByDriver(@PathVariable int driverId) {
		return fuelConsumptionService.getMonthlySpentMoneyByDriver(driverId);
	}
	
	@GetMapping(DETAILS + "/{monthId}/{year}" + DRIVER + "/{driverId}")
	public List<FuelConsumptionDto> getConsumptionDetailsByMonthAndDriver(@PathVariable int monthId, @PathVariable int year, @PathVariable int driverId) {
		return fuelConsumptionService.getConsumptionDetailsByDriver(monthId, year, driverId);
	}
	
	@GetMapping(STATISTICS + "/{year}" + DRIVER + "/{driverId}")
	public List<FuelConsumptionStatDto> getStatisticsByDriver(@PathVariable int year, @PathVariable int driverId) {
		return fuelConsumptionService.getStatisticsByDriver(year, driverId);
	}
	
	@GetMapping(GENERATE_DATA)
	public void saveFuelAndDriver() throws Exception {
		dataGenerator.generateData();
	}
	
}
