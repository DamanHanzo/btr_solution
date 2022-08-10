package btr.assessment.controllers

import btr.assessment.models.ChargeEvent
import btr.assessment.models.Vehicle
import btr.assessment.responses.VehicleChargeDetails
import btr.assessment.services.VehicleChargingService
import btr.assessment.services.VehicleManagementService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.validation.constraints.NotBlank

@Validated
@Controller("/charge")
class VehicleChargingController(
    private val vehicleChargingService: VehicleChargingService,
    private val vehicleManagementService: VehicleManagementService
) {

    @Produces
    @Get
    fun retrieveVehicleChargeDetails(@QueryValue @NotBlank vehicleId: String): HttpResponse<VehicleChargeDetails> {
        if(vehicleManagementService.getVehicle(vehicleId) == null) {
            HttpResponse.ok(VehicleChargeDetails())
        }
        val vehicle: Vehicle = vehicleManagementService.getVehicle(vehicleId)!!
        return HttpResponse.ok(vehicleChargingService.calculateVehicleEmissions(vehicle))
    }

    @Produces
    @Post
    fun createOrUpdateChargeEvent(@Body @NotBlank chargeEvent: ChargeEvent): HttpResponse<String> {
        if(chargeEvent.vehicleId == null) {
            return HttpResponse.badRequest("vehicleId cannot be null")
        }
        if (chargeEvent.startTime == null && chargeEvent.endTime == null) {
            return HttpResponse.badRequest("Both start and end time cannot be null")
        }
        if (vehicleManagementService.getVehicle(vehicleId = chargeEvent.vehicleId) != null) {
           vehicleChargingService.handleChargeEvent(chargeEvent)
        }
            return HttpResponse.ok("Success")
    }
}