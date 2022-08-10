package btr.assessment.services

import btr.assessment.common.Utils
import btr.assessment.common.Utils.Companion.buildVehicleId
import btr.assessment.configurations.DailyRates
import btr.assessment.models.ChargeEvent
import btr.assessment.models.Vehicle
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Named
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek
import java.time.LocalDate

@MicronautTest(propertySources = ["application-test"])
class VehicleChargingServiceImplTest(
    @Named("solar") val solarDailyRates: DailyRates,
    @Named("wind") val windDailyRates: DailyRates,
    @Named("gas") val gasDailyRates: DailyRates,
    @Named("coal") val coalDailyRates: DailyRates,
) {
    @Inject
    lateinit var vehicleChargingService: VehicleChargingService

    @Test
    fun testGetVehicleChargeEmissionsDetails() {
        val vehicle = Vehicle(make = "Honda", model = "Accord")

        val startHour = 10
        val endHour = 12

        //Start Charging
        val vehicleId = buildVehicleId(vehicle)
        val chargeStartEvent = ChargeEvent(vehicleId, startTime = startHour)
        vehicleChargingService.handleChargeEvent(chargeStartEvent)

        //Stop Charging
        val chargeEndEvent = ChargeEvent(vehicleId, endTime = endHour)
        vehicleChargingService.handleChargeEvent(chargeEndEvent)

        val totalChargeTime = endHour - startHour

        val dayToday = LocalDate.now().dayOfWeek
        val vehicleEmissions = vehicleChargingService.calculateVehicleEmissions(vehicle.copy(id = vehicleId))
        assertNotNull(vehicleEmissions)
        assertEquals(vehicle.make, vehicleEmissions?.make)
        assertEquals(vehicle.model, vehicleEmissions?.model)
        assertEquals(totalEmissionRate(totalChargeTime, dayToday, solarDailyRates), vehicleEmissions?.solarEmissions)
        assertEquals(totalEmissionRate(totalChargeTime, dayToday, windDailyRates), vehicleEmissions?.windEmissions)
        assertEquals(totalEmissionRate(totalChargeTime, dayToday, gasDailyRates), vehicleEmissions?.gasEmissions)
        assertEquals(totalEmissionRate(totalChargeTime, dayToday, coalDailyRates), vehicleEmissions?.coalEmissions)
    }

    @Test
    fun testEndHourLowerThanStartHourShouldThrowAnException() {
        val vehicle = Vehicle(make = "Ford", model = "Focus")
        val vehicleId = buildVehicleId(vehicle)

        //Start Charge
        val startChargeEvent = ChargeEvent(vehicleId = vehicleId, startTime = 20)
        vehicleChargingService.handleChargeEvent(startChargeEvent)

        //End Charge
        val endChargeEvent = ChargeEvent(vehicleId = vehicleId, endTime = 19)
        assertThrows<IllegalArgumentException> {
            vehicleChargingService.handleChargeEvent(endChargeEvent)
        }
    }

    @Test
    fun testEndHourEqualToStartHourShouldThrowAnException() {
        val vehicle = Vehicle(make = "Ford", model = "F-150")
        val vehicleId = buildVehicleId(vehicle)

        //Start Charge
        val startChargeEvent = ChargeEvent(vehicleId = vehicleId, startTime = 20)
        vehicleChargingService.handleChargeEvent(startChargeEvent)

        //End Charge
        val endChargeEvent = ChargeEvent(vehicleId = vehicleId, endTime = 20)
        assertThrows<IllegalArgumentException> {
            vehicleChargingService.handleChargeEvent(endChargeEvent)
        }
    }

    @Test
    fun testNullStarHourAndEndHourShouldThrowAnException() {
        val vehicle = Vehicle(make = "Ford", model = "Model T")
        val vehicleId = buildVehicleId(vehicle)

        val chargeEvent = ChargeEvent(vehicleId)

        assertThrows<IllegalArgumentException> {
            vehicleChargingService.handleChargeEvent(chargeEvent)
        }
    }

    private fun totalEmissionRate(totalChargeTime: Int, dayToday: DayOfWeek, dailyRates: DailyRates) =
        totalChargeTime * Utils.getRateByDayOfWeek(dayToday, dailyRates)!!
}