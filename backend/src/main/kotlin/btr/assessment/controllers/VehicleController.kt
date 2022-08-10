package btr.assessment.controllers

import btr.assessment.models.Vehicle
import btr.assessment.services.VehicleManagementService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.constraints.NotBlank

@Validated
@Controller("/vehicle")
class VehicleController(private val vehicleManagementService: VehicleManagementService) {

    @Produces
    @Get("/all")
    fun getAllVehicles(): HttpResponse<List<Vehicle>> {
        return HttpResponse.ok(vehicleManagementService.getAllVehicles())
    }

    @Produces
    @Post
    fun addVehicle(@Body @NotBlank vehicle: Vehicle): HttpResponse<Map<String, String>> {
        val vehicleId = vehicleManagementService.addVehicle(vehicle)
        return HttpResponse.ok(Collections.singletonMap("vehicleId", vehicleId))
    }
}