# BikeMarket

   BitMarket is web application creation to menage bike fleet. With admin permision we can add/remove bike from the data base.
For particular vehicle we are able to change parameters. Application allow also track actual rent status provide us necessary information like: person name, bike type and prices.


## Solution

The data base is situated in external amazon database.
Used backet "jdbc:postgresql://mytestdatabase.cd5sl8ldadft.eu-central-1.rds.amazonaws.com:6378/postgres"

## Prerequisites
  
Java 11  
Apache Maven 3.x

# Access Process

To gain fully application access You have to regist yourself to database by regist buttun and following next steps.

![alt text1](https://raw.githubusercontent.com/potepa606/BikeMarket/master/src/main/resources/static/images/Readme_Login.PNG)


# Main Functionality - details description

BitMarket web application has four tab.
 
:one:  Main

:two: Add Bike

:three: Edit

:four: Renatals

## Main

Here You can find general information and bikes summary.

![alt text1](https://github.com/potepa606/BikeMarket/blob/master/src/main/resources/static/images/Readme_add.PNG)

## Add Bike

In this sheets You can upload new bike to database. 
There are three obligatory field to complete.

- Name of bike
- Price [zł] / day 
- Bike photo

![alt text1](https://github.com/potepa606/BikeMarket/blob/master/src/main/resources/static/images/Readme_Rent.PNG)

## Edit

In this place You have overview about all rent bike. 


## Rentals

There You have summarry board. 

![alt text1](https://github.com/potepa606/BikeMarket/blob/master/src/main/resources/static/images/Readme_summary.PNG)
