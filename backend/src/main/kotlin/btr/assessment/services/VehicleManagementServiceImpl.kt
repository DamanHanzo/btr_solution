package btr.assessment.services

import btr.assessment.common.Utils.Companion.buildVehicleId
import btr.assessment.models.Vehicle
import jakarta.inject.Singleton

@Singleton
class VehicleManagementServiceImpl : VehicleManagementService {
    private val vehicleMap: MutableMap<String, Vehicle> = mutableMapOf()

    override fun addVehicle(vehicle: Vehicle): String {
        validateVehicle(vehicle)
        checkForDuplicateVehicles(vehicle)
        val vehicleId = buildVehicleId(vehicle)
        vehicleMap[vehicleId] = vehicle.copy(id = vehicleId)
        return vehicleId
    }

    override fun getVehicle(vehicleId: String): Vehicle? {
        if (!vehicleMap.contains(vehicleId)) {
            return null
        }
        return vehicleMap[vehicleId]
    }

    override fun getAllVehicles(): List<Vehicle> {
        return vehicleMap.map {
            Vehicle(
                id = it.key,
                make = it.value.make,
                model = it.value.model
            )
        }
    }

    private fun validateVehicle(vehicle: Vehicle) {
        require(vehicle.make.isNotEmpty()) { "Make cannot be empty" }
        require(vehicle.model.isNotEmpty()) { "Model cannot be empty" }
    }

    private fun checkForDuplicateVehicles(vehicle: Vehicle) {
        val hasDuplicate = vehicleMap.containsKey(buildVehicleId(vehicle))
        if (hasDuplicate) {
            throw IllegalArgumentException("Vehicle already exists")
        }
    }
}