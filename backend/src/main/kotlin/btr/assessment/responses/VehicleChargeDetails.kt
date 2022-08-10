package btr.assessment.responses

data class VehicleChargeDetails(
    val vehicleId: String? = null,
    val make: String? = null,
    val model: String? = null,
    val startTime: Int? = null,
    val endTime: Int? = null,
    val solarEmissions: Int? = null,
    val windEmissions: Int? = null,
    val gasEmissions: Int? = null,
    val coalEmissions: Int? = null
)