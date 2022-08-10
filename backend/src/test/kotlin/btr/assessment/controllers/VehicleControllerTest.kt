package btr.assessment.controllers

import btr.assessment.common.Utils.Companion.buildVehicleId
import btr.assessment.models.Vehicle
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

@MicronautTest
class VehicleControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testAddVehicle() {
        val vehicleToAdd = Vehicle(make = "Honda", model = "CR-V")
        val request: HttpRequest<Any> = HttpRequest.POST("vehicle", convertToJson(vehicleToAdd))
        val responseJson = client.toBlocking().retrieve(request)
        assertNotNull(responseJson)
        val responseMap: Map<String, String> = jacksonObjectMapper().readValue(responseJson)
        assertEquals(responseMap["vehicleId"], buildVehicleId(vehicleToAdd))
    }

    @Test
    fun testAddingEmptyVehicleReturnsBadRequestError() {
        val e = Executable { client.toBlocking().retrieve(HttpRequest.POST("vehicle", ""), HttpStatus::class.java) }
        val thrown = assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    fun testAddingEmptyVehicleReturnsInternalServerError() {
        val e = Executable {
            client.toBlocking().retrieve(
                HttpRequest.POST("vehicle", convertToJson(Vehicle(make = "", model = ""))),
                HttpStatus::class.java
            )
        }
        val thrown = assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.status)
    }

    private fun convertToJson(vehicleToAdd: Vehicle) =
        jacksonObjectMapper().writeValueAsString(vehicleToAdd)
}