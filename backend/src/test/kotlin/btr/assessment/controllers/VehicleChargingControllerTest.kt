package btr.assessment.controllers

import btr.assessment.common.Utils.Companion.buildVehicleId
import btr.assessment.models.ChargeEvent
import btr.assessment.models.Vehicle
import btr.assessment.services.VehicleManagementServiceImpl
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable

@MicronautTest
@ExtendWith(MockKExtension::class)
class VehicleChargingControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @MockK
    lateinit var vehicleManagementService: VehicleManagementServiceImpl

    @Test
    fun testCreateStartChargeEvent() {
        val vehicle = Vehicle(make = "Honda", model = "Accord")
        val vehicleId = buildVehicleId(vehicle)
        every { vehicleManagementService.getVehicle(any()) } returns vehicle.copy(id = vehicleId)
        val chargeEvent = ChargeEvent(vehicleId = vehicleId, startTime = 10)

        val chargeRequest: HttpRequest<Any> = HttpRequest.POST("charge", chargeEvent)
        val responseJson = client.toBlocking().retrieve(chargeRequest)
        assertNotNull(responseJson)
    }

    @Test
    fun testNullVehicleIdReturnsBadRequest() {
        val chargeEvent = ChargeEvent(startTime = 10)
        val e = Executable { client.toBlocking().retrieve(HttpRequest.POST("charge", chargeEvent), HttpStatus::class.java) }
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    fun testNullStartAndEndTimeReturnsBadRequest() {
        val chargeEvent = ChargeEvent(vehicleId = "vehicle_id")
        val e = Executable { client.toBlocking().retrieve(HttpRequest.POST("charge", chargeEvent), HttpStatus::class.java) }
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }
}