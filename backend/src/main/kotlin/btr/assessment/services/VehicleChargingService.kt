package btr.assessment.services

import btr.assessment.models.ChargeEvent
import btr.assessment.models.Vehicle
import btr.assessment.responses.VehicleChargeDetails
import io.micronaut.context.annotation.DefaultImplementation

@DefaultImplementation(VehicleChargingServiceImpl::class)
interface VehicleChargingService {
    fun handleChargeEvent(chargeEvent: ChargeEvent)
    fun calculateVehicleEmissions(vehicle: Vehicle): VehicleChargeDetails?
}