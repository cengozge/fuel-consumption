package ee.swedbank.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ee.swedbank.application.workers.DataGenerator;

@SpringBootApplication
@EnableJpaRepositories
public class FuelConsumptionApplication {

    public static void main(String[] args) {
    	ConfigurableApplicationContext context = SpringApplication.run(FuelConsumptionApplication.class, args);
    	try {
			context.getBean(DataGenerator.class).generateData();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
