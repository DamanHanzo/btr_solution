import axios from 'axios';
import { useEffect, useState } from 'react';
import { Alert } from 'react-bootstrap';
import Table from 'react-bootstrap/Table';

const BASE_URI = process.env.REACT_APP_BACKEND_API

function SimpleGrid({ updateGrid, captureVehicle }) {
  const [vehicleDetails, setVehicles] = useState([]);
  const [error, setError] = useState(null);
  const getVehicleDetails = async (vehicles) => {
    const details = [];
    for (const vehicle of vehicles) {
      try {
        const vehicleResponse = await axios({
          method: 'get',
          url: BASE_URI + '/charge',
          params: {
            vehicleId: vehicle.id
          }
        })
        details.push(vehicleResponse.data)
      } catch (err) {
        console.error(error)
        setError("Got an while retrieving details")
      }
    }
    setVehicles(details)
  }

  useEffect(() => {
    const getVehicles = () => {
      axios({
        method: 'get',
        url: BASE_URI + '/vehicle/all'
      }).then(vehicleResponse => {
        getVehicleDetails(vehicleResponse.data)
        captureVehicle(vehicleResponse.data)
      }).catch(error => {
        console.error(error)
        setError("Got an while retrieving while details")
      })
    };
    getVehicles()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [updateGrid])

  return (
    <div>
      <h2>Vehicle Emissions Summary</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Make</th>
            <th>Model</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Solar Emissions</th>
            <th>Wind Emissions</th>
            <th>Gas Emissions</th>
            <th>Coal Emissions</th>
          </tr>
        </thead>
        <tbody>
          {vehicleDetails && vehicleDetails.map(detail =>
            <tr key={detail.vehicleId}>
              <td>{detail.vehicleId}</td>
              <td>{detail.make}</td>
              <td>{detail.model}</td>
              <td>{detail.startTime}</td>
              <td>{detail.endTime}</td>
              <td>{detail.solarEmissions}</td>
              <td>{detail.windEmissions}</td>
              <td>{detail.gasEmissions}</td>
              <td>{detail.coalEmissions}</td>
            </tr>)}
        </tbody>
      </Table>
      {error && <Alert variant="danger" >
        <Alert.Heading>Failed to retrieve vehicle details.</Alert.Heading>
        <p>{error}</p>
      </Alert>}
    </div>
  );
}

export default SimpleGrid;