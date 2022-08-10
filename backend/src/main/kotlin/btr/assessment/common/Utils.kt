package btr.assessment.common

import btr.assessment.configurations.DailyRates
import btr.assessment.models.Vehicle
import java.time.DayOfWeek

class Utils {
    companion object {
        fun buildVehicleId(vehicle: Vehicle): String = "${vehicle.make}:${vehicle.model}"
        fun getRateByDayOfWeek(dayOfWeek: DayOfWeek, dailyRates: DailyRates): Int? {
            when (dayOfWeek) {
                DayOfWeek.SUNDAY -> {
                    return dailyRates.sunday
                }
                DayOfWeek.MONDAY -> {
                    return dailyRates.monday
                }
                DayOfWeek.TUESDAY -> {
                    return dailyRates.tuesday
                }
                DayOfWeek.WEDNESDAY -> {
                    return dailyRates.wednesday
                }
                DayOfWeek.THURSDAY -> {
                    return dailyRates.thursday
                }
                DayOfWeek.FRIDAY -> {
                    return dailyRates.friday
                }
                DayOfWeek.SATURDAY -> {
                    return dailyRates.saturday
                }
            }
        }
    }
}