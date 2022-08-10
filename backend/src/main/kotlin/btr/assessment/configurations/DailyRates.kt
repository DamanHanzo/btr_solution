package btr.assessment.configurations

import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import io.micronaut.core.annotation.Introspected

@Introspected
@EachProperty("emission-rates")
class DailyRates
constructor(
    @param:Parameter val name: String
) {
    var sunday: Int? = null
    var monday: Int? = null
    var tuesday: Int? = null
    var wednesday: Int? = null
    var thursday: Int? = null
    var friday: Int? = null
    var saturday: Int? = null
}