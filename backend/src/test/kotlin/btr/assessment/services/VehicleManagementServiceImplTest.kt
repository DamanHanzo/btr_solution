package btr.assessment.services

import btr.assessment.models.Vehicle
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class VehicleManagementServiceImplTest {
    @Inject
    lateinit var vehicleManagementService: VehicleManagementService

    @Test
    @Order(1)
    fun testAddingVehicle() {
        val vehicleToAdd = Vehicle(make = "Honda", model = "Accord")
        assertNotNull(vehicleManagementService.addVehicle(vehicleToAdd))
    }

    @Test
    @Order(2)
    fun testCreatingVehicleWithBlankMakeAndModelThrowsException() {
        assertThrows<IllegalArgumentException> {
            vehicleManagementService.addVehicle(Vehicle(make = "", model = ""))
        }
    }

    @Test
    @Order(3)
    fun testCreatingVehicleWithBlankMakeThrowsException() {
        assertThrows<IllegalArgumentException> {
            vehicleManagementService.addVehicle(Vehicle(make = "", model = "Focus"))
        }
    }

    @Test
    @Order(4)
    fun testCreatingVehicleWithBlankModelThrowsException() {
        assertThrows<IllegalArgumentException> {
            vehicleManagementService.addVehicle(Vehicle(make = "Ferrari", model = ""))
        }
    }

    @Test
    @Order(6)
    fun testAddingDuplicateVehicle() {
        val vehicleToAddDuplicate = Vehicle(make = "Honda", model = "Accord")
        assertThrows<IllegalArgumentException> {
            vehicleManagementService.addVehicle(vehicleToAddDuplicate)
        }
    }

    @Test
    @Order(7)
    fun testCreatingSameVehicleUpperCasedAndLowerCasedShouldThrowException() {
        val vehicleCapitalized = Vehicle(make = "Chevrolet", model = "Denali")
        assertNotNull(vehicleManagementService.addVehicle(vehicleCapitalized))
        val vehicleLowerCased = Vehicle(make = "chevrolet", model = "denali")
        assertThrows<IllegalArgumentException> {
            vehicleManagementService.addVehicle(vehicleLowerCased)
        }
    }
}