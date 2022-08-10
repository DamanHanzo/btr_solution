package btr.assessment.requests

data class ChargeRequest(val vehicleId: String, val startTime: Int? = null, val endTime: Int? = null)