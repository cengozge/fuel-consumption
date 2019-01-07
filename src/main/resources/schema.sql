create table T_DRIVER (ID number not null,
                        NAME varchar(100),
                        primary key (ID));

create table T_FUEL_CONSUMPTION (ID number not null,
                                 FUELTYPE varchar(20) not null,
                                 PRICEPERLITER number not null,
                                 VOLUME number not null,
                                 CONSUMPTIONDATE date not null,
                                 DRIVERID number not null,
                                 primary key (ID));
                                 
 create sequence consumption_sequence start with 1 increment by 1;
