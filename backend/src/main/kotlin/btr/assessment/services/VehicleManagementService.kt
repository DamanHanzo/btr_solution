package btr.assessment.services

import btr.assessment.models.Vehicle
import io.micronaut.context.annotation.DefaultImplementation
import java.util.*

@DefaultImplementation(VehicleManagementServiceImpl::class)
interface VehicleManagementService {
    fun addVehicle(vehicle: Vehicle): String
    fun getVehicle(vehicleId: String): Vehicle?
    fun getAllVehicles(): List<Vehicle>
}