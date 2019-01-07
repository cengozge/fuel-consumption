package ee.swedbank.application.workers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ee.swedbank.application.dtos.FuelConsumptionDto;

@Service
public class FileProcessor {

	public List<FuelConsumptionDto> getFuelConsumptionListFromFile(byte[] bytes) {
		List<FuelConsumptionDto> consumptionList = new ArrayList<>();
		try (InputStream inputStream = new ByteArrayInputStream(bytes);
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {
			// first line is header, skip it
			br.lines().skip(1).map(line -> line.split(",")).forEach(line -> {
				FuelConsumptionDto consumptionDto = FuelConsumptionDto.builder().fuelType(line[0])
						.pricePerLiter(new BigDecimal(line[1])).volume(new BigDecimal(line[2]))
						.consumptionDate(LocalDate.parse(line[3])).driverId(new Long(line[4])).build();

				consumptionList.add(consumptionDto);
			});

		} catch (Exception e) {
		}
		return consumptionList;
	}

}
