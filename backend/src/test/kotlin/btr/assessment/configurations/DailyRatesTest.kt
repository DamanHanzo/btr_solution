package btr.assessment.configurations

import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DailyRatesTest {
    @Test
    fun testEmissionsConfiguration() {
        val items: MutableMap<String, Any> = HashMap()
        items["emission-rates.solar.sunday"] = 2
        items["emission-rates.wind.sunday"] = 1
        items["emission-rates.coal.sunday"] = 100
        items["emission-rates.gas.sunday"] = 80

        val ctx = ApplicationContext.run(items)

        val solarConfiguration = ctx.getBean(DailyRates::class.java, Qualifiers.byName("solar"))
        val windConfiguration = ctx.getBean(DailyRates::class.java, Qualifiers.byName("wind"))
        val coalConfiguration = ctx.getBean(DailyRates::class.java, Qualifiers.byName("coal"))
        val gasConfiguration = ctx.getBean(DailyRates::class.java, Qualifiers.byName("gas"))

        assertEquals("solar", solarConfiguration.name)
        assertEquals(2, solarConfiguration.sunday)

        assertEquals("wind", windConfiguration.name)
        assertEquals(1, windConfiguration.sunday)

        assertEquals("coal", coalConfiguration.name)
        assertEquals(100, coalConfiguration.sunday)

        assertEquals("gas", gasConfiguration.name)
        assertEquals(80, gasConfiguration.sunday)

        ctx.close()
    }
}