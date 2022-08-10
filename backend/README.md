## BTR Vehicle Emissions Calculator

Logic for handling of setting up of vehicles for charging, and calculating related emissions per each energy source.

### Requirements

- Quick start guide: https://micronaut.io/download/
- Java 17 with Graal VM `22.1.0.r17-grl`
  - Recommend using sdkman(`sdk install java 22.1.0.r17-grl`)
- Maven
- mn cli
---

### Run Locally

- Simply issue the following command from terminal: `./mvnw mn:run`.
- Service by default listens on port 8080.
---

### API Reference
#### Add/Create Vehicle(`POST`)

- Endpoint for adding a vehicle
- URI: `/vehicle`
- Accepts(request body): 
  - make: Vehicle's make e.g. honda, ford, volkswagen, etc.
    - Required
  - model: Vehicle's model e.g bronco, wrangler, focus, etc.
    - Required
- Returns(response):
  - `vehicleId`: Id of the successfully added vehicle

#### Get All Vehicles(`GET`)

- Endpoint for retrieving all vehicles currently in the app
- Accepts: Nothing
- Returns(response): list of all vehicles maintained in the app

#### Create/Updating Charge Event(`POST`)

- Endpoint for adding/updating vehicle charge events
- Accepts(request body):
  - `vehicleId`: Id of vehicle to register the charge event for
    - Required
  - `startTime`: Hour of the day to initiate charging
    - Optional
  - `endTime`: Hour of the day to conclude charging
    - Optional
  - **Note**: One of the two `startTime` or `endTime` need to be specified. Both cannot be null.
- Returns(response):
 - Success Http Status(200 OK) with response body if successful

#### Retrieve Charge Event Details(`GET`)

- Endpoint for retrieving vehicle emissions summary details
- Accepts(query param)
  - `vehicleId`: Id of the registered vehicle
- Returns(response):
  - `make`: make of the registered vehicle
  - `model`: model of the register vehicle
  - `vehicleId`: Id of the register vehicle
  - `startTime`: time charge was kicked off
  - `endTime`: time charge was concluded
  - `solarEmissions`: total emissions created by solar energy consumption during charge
  - `windEmissions`: total emissions created by wind energy consumption during charge
  - `gasEmissions`: total emissions created by gas energy consumption during charge
  - `coalEmissions`: total emissions created by coal energy consumption during charge