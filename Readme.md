DESIGN POINTS
Term Explanations:
1. DTO term is used for data presentation object.
2. DAO term is used for Data Access Object

Assumptions and Explanations:
Explanation 1. Save pricePerLitter in FuelConsumption table in order to ease calculations

Explanation 2. Implement business logic such as calculation of total price etc. in server side in order to separate it from db logic. If multiplication was done in query, then it would not be easy to apply changes if necessary in future. 
registration is not clear -in terms of order of attributes, if it is the same given in the pdf or changable-, I chose not to create a separate table.

Explanation 3. Why I did not use spring jpa repository queries: to be able to return a different data presentation object type from data access layer; I had to use constructor in query or projection interface which are difficult to maintain. So I decided to use HQL which is easier to maintain and a side-effect such as change in any entity is not applied to custom query in compile time so that it gives only runtime error.

Explanation 4. Regarding to explanation 3; Hibernate 6 -which is not released yet- will replace ResultTransformer with a functional interface, so now I have to use deprecated method

Explanation 5. Assumption for Functional Req. 1- To save fuel consumption:
	- First, system queries the driver by driverId that driverId is provided in fuel consumption.
	- If driver is found in driver's table, then it saves fuel query; else throws an exception with a "driver not found" message. So you have to save a valid driver.
Explanation 6. Assumption for Functional Req. 1- Insertion of bulk data from file:
	- File content is supposed to be in the format defined below:
		* Header as first line of the file will be skipped during reading file
		* Starting from second line, the data order is ${fuelType},${pricePerLiter},${volume},${date},${driverId} as comma separated values and of the data type
			fuelType values	FUEL_95, FUEL_98, FUEL_D
			pricePerLiter 	BigDecimal
			volume 			BigDecimal
			date			LocalDate of format yyyy-dd-MM
			driverId		Long 
	- Multiple insertion of the data in the file is allowed since defining a businessKey and matching that key with each line is not in the scope of this application.
	This means, the file is processed multiple times and data will be duplicated.
	
Explanation 7. Why I calculate the Functional Req. 2 in db query instead of server side: 
	- pretend not to change & easy to maintain

Explanation 8. Why I chose to implement Functional Req. 3 in db query instead of using filter on server side:
	- not to select whole amount of data

Explanation 9. Why I added year path variable to endpoint of Functional Req. 3 :
	- month without the year is meaningless

## Entity design
Enum fuel type(95, 98, D)
BigDecimal totalPrice        		in EUR 	(Ex. 10.10)
BigDecimal volume 			 	in litters 	(Ex. 12.5)
LocalDate date 							   	(Ex. 01.21.2018)
long driverID 							   	(Ex. 12345)

## Technical Req. 1- The fuel consumption data should be stored locally in the file or in embedded database, but it should possible later to swap current data storage to something else (MySql, Oracle DB etc).
This directs me to configure db settings in .yml as H2 for now and Oracle, MySQL with empty values and set active profile as profile of H2

## Technical Req. 2- The application initially will have only RESTful API with JSON payloads in response, but won't be limited only to this interface in the future.
This directs me to return List or String which is converted to JSON by framework.

## Technical Req. 3- Data validation should be performed before persisting and corresponding error message should be returned in case of invalid input.
This directs me to use @Valid annotation in Request body.

## Functional Req. 1- It should be possible to register 
-one single record: This directs me to design a POST endpoint to register one record
POST localhost:8080/consumption/fuelConsumption followed by a JSON of FuelConsumptionDto

-bulk consumption (multiple records in one file, for example multipart/form-data): This directs me to design a POST endpoint which accepts
POST localhost:8080/consumption/ followed by a csv file of format given in Explanation 6.

## Functional Req. 2- Total spent amount of money grouped by month
GET localhost:8080/consumption/monthlySpentMoney

## Functional Req. 3- Fuel consumption details for specified month ( each row should contain: fuelType, volume, date, pricePerLiter, totalPrice, driverID)
GET localhost:8080/consumption/details/{monthId}/{year}  why year added is explained in Explanation 9.

## Functional Req. 4- statistics for each month, list fuel consumption records grouped by fuel type (each row should contain: fuel type, volume, average price, total price)
GET localhost:8080/consumption/statistics/{year}

Every request can be made either for all drivers or for the specific driver (identified by id).

## Functional Req. 5- Total spent amount of money grouped by month by driverID
GET localhost:8080/consumption/monthlySpentMoney/driver/{driverID}

## Functional Req. 6- Fuel consumption details for specified month by driverID
GET localhost:8080/consumption/details/{monthId}/{year}/driver/{driverID}

## Functional Req. 7- statistics for each month, list fuel consumption records grouped by fuel type by driverID
GET localhost:8080/consumption/statistics/{year}/driver/{driverID}

	
	
//TODO improvements
1. number format
2. hibernate column naming strategy
3. Relation for fuelConsumption.driverId with driver entity
4. insert scripts into file
	