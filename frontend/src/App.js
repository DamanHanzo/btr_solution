import './App.css';
import SimpleGrid from './components/SimpleGrid';
import AddVehicle from './components/AddVehicle';
import ChargeVehicle from './components/ChargeVehicle';
import { Stack } from 'react-bootstrap';
import { useState } from 'react';

function App() {
  const [updateGrid, setUpdateGrid] = useState(null)
  const [vehicles, setVehicles] = useState(null)
  return (
    <div className='container'>
      <SimpleGrid updateGrid={updateGrid} captureVehicle={(vhcls) => setVehicles(vhcls)}/>
      <Stack direction="horizontal" gap={3}>
        <AddVehicle handleNewVehicle={(shouldUpdateGrid) => setUpdateGrid(shouldUpdateGrid)}/>
        <ChargeVehicle vehicles={vehicles} handleNewChargeEvent={(shouldUpdateGrid) => setUpdateGrid(shouldUpdateGrid)} />
      </Stack>
    </div>
  );
}

export default App;
