package btr.assessment.services

import btr.assessment.common.Utils
import btr.assessment.configurations.DailyRates
import btr.assessment.models.ChargeEvent
import btr.assessment.models.Vehicle
import btr.assessment.responses.VehicleChargeDetails
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.time.DayOfWeek
import java.time.LocalDate

@Singleton
class VehicleChargingServiceImpl(
    @Named(SOLAR) val solarConfiguration: DailyRates,
    @Named(WIND) val windConfiguration: DailyRates,
    @Named(COAL) val coalConfiguration: DailyRates,
    @Named(GAS) val gasConfiguration: DailyRates
) : VehicleChargingService {
    private val vehicleChargeStatusMap: MutableMap<String, ChargeEvent> = HashMap()

    override fun handleChargeEvent(chargeEvent: ChargeEvent) {
        if (chargeEvent.startTime == null && chargeEvent.endTime == null) {
            throw IllegalArgumentException("Start time and end time can both be null")
        } else if (chargeEvent.startTime != null && chargeEvent.endTime == null) {
            startCharging(chargeEvent)
        } else if (chargeEvent.endTime != null && chargeEvent.startTime == null) {
            stopCharging(chargeEvent)
        } else {
            startCharging(chargeEvent)
            stopCharging(chargeEvent)
        }
    }

    private fun startCharging(chargeEvent: ChargeEvent) {
        if (vehicleChargeStatusMap.containsKey(chargeEvent.vehicleId)) {
            throw RuntimeException("Vehicle ${chargeEvent.vehicleId} is already charging")
        }
        vehicleChargeStatusMap[chargeEvent.vehicleId!!] = chargeEvent
    }

    private fun stopCharging(chargeEvent: ChargeEvent) {
        val vehicleId = chargeEvent.vehicleId
        if (!vehicleChargeStatusMap.containsKey(vehicleId)) {
            throw IllegalArgumentException("Vehicle is not charging")
        }
        val existingChargeEvent = vehicleChargeStatusMap[vehicleId]
        if (existingChargeEvent != null && chargeEvent.endTime != null
            && existingChargeEvent.startTime != null
            && existingChargeEvent.startTime >= chargeEvent.endTime
        ) {
            throw IllegalArgumentException("Charge end time has to be more than start time")
        }
        val updatedChargeEvent = ChargeEvent(
            vehicleId = existingChargeEvent!!.vehicleId,
            startTime = existingChargeEvent.startTime,
            endTime = chargeEvent.endTime
        )
        vehicleChargeStatusMap[vehicleId!!] = updatedChargeEvent
    }


    override fun calculateVehicleEmissions(vehicle: Vehicle): VehicleChargeDetails? {
        if (!vehicleChargeStatusMap.containsKey(vehicle.id)) {
            return VehicleChargeDetails(
                vehicleId = vehicle.id,
                make = vehicle.make,
                model = vehicle.model
            )
        }
        val chargeEvent = vehicleChargeStatusMap[vehicle.id]
        if (chargeEvent?.startTime == null) {
            throw RuntimeException("Vehicle was not setup for charging: $vehicle")
        }
        if (chargeEvent.endTime != null) {
            val totalChargeTime = chargeEvent.endTime - chargeEvent.startTime
            val day = LocalDate.now().dayOfWeek
            return VehicleChargeDetails(
                vehicleId = vehicle.id,
                make = vehicle.make,
                model = vehicle.model,
                startTime = chargeEvent.startTime,
                endTime = chargeEvent.endTime,
                solarEmissions = getTotalEmissions(totalChargeTime, getRateByEnergySourceAndDay(SOLAR, day)),
                windEmissions = getTotalEmissions(totalChargeTime, getRateByEnergySourceAndDay(WIND, day)),
                coalEmissions = getTotalEmissions(totalChargeTime, getRateByEnergySourceAndDay(COAL, day)),
                gasEmissions = getTotalEmissions(totalChargeTime, getRateByEnergySourceAndDay(GAS, day))
            )
        }
        return VehicleChargeDetails(
            vehicleId = vehicle.id,
            make = vehicle.make,
            model = vehicle.model,
            startTime = chargeEvent.startTime
        )
    }

    private fun getRateByEnergySourceAndDay(source: String, dayOfWeek: DayOfWeek): Int? {
        return when (source) {
            SOLAR -> {
                Utils.getRateByDayOfWeek(dayOfWeek, solarConfiguration)
            }
            WIND -> {
                Utils.getRateByDayOfWeek(dayOfWeek, windConfiguration)
            }
            COAL -> {
                Utils.getRateByDayOfWeek(dayOfWeek, coalConfiguration)
            }
            GAS -> {
                Utils.getRateByDayOfWeek(dayOfWeek, gasConfiguration)
            }
            else ->
                throw RuntimeException("Couldn't resolve configuration by energy source")
        }
    }

    private fun getTotalEmissions(totalChargeTime: Int, rate: Int?) =
        totalChargeTime * rate!!

    companion object {
        const val SOLAR = "solar"
        const val WIND = "wind"
        const val GAS = "gas"
        const val COAL = "coal"
    }
}
