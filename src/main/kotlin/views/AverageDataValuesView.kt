package views

import data.DataBaseHandler
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import tornadofx.*
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.sqrt

class AverageDataValuesView : View("Average Values") {

    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()

    private val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7) }
    private val toDatepicker = datepicker { value = LocalDate.now() }
    private val showFilteredData = SimpleBooleanProperty(false)

    private val tempVal = Label("Value: ")
    private val humidityVal = Label("Value: ")
    private val pressureVal = Label("Value: ")
    private val oxygenVal = Label("Value: ")
    private val salinityVal = Label("Value: ")
    private val turbidityVal = Label("Value: ")
    private val waterTempVal = Label("Value: ")
    init {
        showFilteredData.addListener { _, _, newValue ->
            if (newValue) {
                updateFilteredData()
            } else {
                updateAllData()
            }
        }

        fromDatepicker.valueProperty().addListener { _, _, _ ->
            if (showFilteredData.get()) {
                updateFilteredData()
            }
        }

        toDatepicker.valueProperty().addListener { _, _, _ ->
            if (showFilteredData.get()) {
                updateFilteredData()
            }
        }
    }

    override val root = vbox {
        paddingAll = 20.0
        alignment = Pos.CENTER
        hbox(10.0) {
            alignment = Pos.TOP_CENTER
            label("Values")
            padding = insets(0.0, 0.0, 10.0, 0.0)
        }
        hbox(10.0) {
            alignment = Pos.CENTER
            label("From Date:")
            add(fromDatepicker)
            label("To Date:")
            add(toDatepicker)
            checkbox("Filter", showFilteredData)
        }
        vbox(30.0) {
            padding = insets(50.0, 0.0, 0.0, 5.0)
            alignment = Pos.CENTER_LEFT
            hbox(50.0) {
                label("Temperature")
                add(tempVal)
            }
            hbox(50.0) {
                label("Humidity")
                add(humidityVal)
            }
            hbox(50.0) {
                label("Pressure")
                add(pressureVal)
            }
            hbox(50.0) {
                label("Oxygen Content")
                add(oxygenVal)
            }
            hbox(50.0) {
                label("Salinity")
                add(salinityVal)
            }
            hbox(50.0) {
                label("Turbidity")
                add(turbidityVal)
            }
            hbox(50.0) {
                label("Water Temperature")
                add(waterTempVal)
            }
        }
    }

    private fun List<Int?>.averageN(): Double {
        val nonNullValues = filterNotNull()
        return if (nonNullValues.isEmpty()) {
            0.0
        } else {
            nonNullValues.sum().toDouble() / nonNullValues.size
        }
    }

    private fun List<Int?>.standardDeviation(): Double {
        val nonNullValues = filterNotNull()
        val average = nonNullValues.averageN()
        val variance = if (nonNullValues.size <= 1) {
            0.0
        } else {
            nonNullValues.sumOf { (it - average) * (it - average) } / (nonNullValues.size - 1)
        }
        return sqrt(variance)
    }

    private fun updateFilteredData() {
        val filteredData = sensorDataList.filter {
            it.timestamp in fromDatepicker.value.atStartOfDay()..toDatepicker.value.atTime(
                LocalTime.MAX)
        }
        val temperatureValues = filteredData.map { it.temperature }
        val humidityValues = filteredData.map { it.humidity }
        val pressureValues = filteredData.map { it.pressure }
        val oxygenContentValues = filteredData.map { it.oxygenContent }
        val salinityValues = filteredData.map { it.salinity }
        val turbidityValues = filteredData.map { it.turbidity }
        val waterTemperatureValues = filteredData.map { it.waterTemperature }

        tempVal.text = "Average: ${temperatureValues.averageN()}"
        humidityVal.text = "Average: ${humidityValues.averageN()}"
        pressureVal.text = "Average: ${pressureValues.averageN()}"
        oxygenVal.text = "Average: ${oxygenContentValues.averageN()}"
        salinityVal.text = "Average: ${salinityValues.averageN()}"
        turbidityVal.text = "Average: ${turbidityValues.averageN()}"
        waterTempVal.text = "Average: ${waterTemperatureValues.averageN()}"
    }

    private fun updateAllData() {
        val temperatureValues = sensorDataList.map { it.temperature }
        val humidityValues = sensorDataList.map { it.humidity }
        val pressureValues = sensorDataList.map { it.pressure }
        val oxygenContentValues = sensorDataList.map { it.oxygenContent }
        val salinityValues = sensorDataList.map { it.salinity }
        val turbidityValues = sensorDataList.map { it.turbidity }
        val waterTemperatureValues = sensorDataList.map { it.waterTemperature }
        tempVal.text = "Average: ${temperatureValues.averageN()}"
        humidityVal.text = "Average: ${humidityValues.averageN()}"
        pressureVal.text = "Average: ${pressureValues.averageN()}"
        oxygenVal.text = "Average: ${oxygenContentValues.averageN()}"
        salinityVal.text = "Average: ${salinityValues.averageN()}"
        turbidityVal.text = "Average: ${turbidityValues.averageN()}"
        waterTempVal.text = "Average: ${waterTemperatureValues.averageN()}"
    }
}