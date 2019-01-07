package ee.swedbank.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {

	private String name;
	
	@Override
	public String toString() {
		return this.getName() + " ";
	}
}
