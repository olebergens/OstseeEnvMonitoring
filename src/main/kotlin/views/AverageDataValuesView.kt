package views

import data.DataBaseHandler
import javafx.geometry.Pos
import tornadofx.*
import java.time.LocalDate
import kotlin.math.sqrt

class AverageDataValuesView : View("Average Values") {

    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()

    override val root = vbox {
        paddingAll = 20.0
        alignment = Pos.CENTER

        label("Average Values")
        hbox(10.0) {
            alignment = Pos.CENTER
            label("From Date:")
            val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7)}
            label("To Date:")
            val toDatepicker = datepicker { value = LocalDate.now() }
            button("Show") {
                action {
                    val startDate = fromDatepicker.value.atStartOfDay()
                    val endDate = fromDatepicker.value.atStartOfDay().plusDays(1)

                    val filteredData = sensorDataList.filter { it.timestamp in startDate..endDate}

                    val temperatureData = filteredData.mapNotNull { it.temperature }
                    val humidityData = filteredData.mapNotNull { it.humidity }
                    val pressureData = filteredData.mapNotNull { it.pressure }

                    val temperatureAverage = temperatureData.average()
                    val humidityAverage = humidityData.average()
                    val pressureAverage = pressureData.average()

                    val temperatureStdDev = temperatureData.standardDeviation()
                    val humidityStdDev = humidityData.standardDeviation()
                    val pressureStdDev = pressureData.standardDeviation()

                    label("Temperature: Average = ${temperatureAverage}, Standard Deviation = ${temperatureStdDev}")
                    label("Humidity: Average = ${humidityAverage}, Standard Deviation = ${humidityStdDev}")
                    label("Pressure: Average = ${pressureAverage}, Standard Deviation = ${pressureStdDev}")

                }
            }
        }
    }

    private fun List<Double>.average(): Double {
        return if (isEmpty()) {
            0.0
        } else {
            sum() / size
        }
    }

    private fun List<Int>.standardDeviation(): Double {
        val average = average()
        val variance = if (size <=1) {
            0.0
        } else {
            sumOf { (it - average) * (it - average) } / (size - 1)
        }
        return sqrt(variance)
    }
}