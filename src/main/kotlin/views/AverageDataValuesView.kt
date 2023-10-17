package views

import data.DataBaseHandler
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import tornadofx.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.sqrt

class AverageDataValuesView : View("Average / Standard Deviation") {

    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()

    private val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7) }
    private val toDatepicker = datepicker { value = LocalDate.now() }
    private val showFilteredData = SimpleBooleanProperty(false)

    private val tempVal = Label("Average: ")
    private val humidityVal = Label("Average: ")
    private val pressureVal = Label("Average: ")
    private val oxygenVal = Label("Average: ")
    private val salinityVal = Label("Average: ")
    private val turbidityVal = Label("Average: ")
    private val waterTempVal = Label("Average: ")


    private val tempValStd = Label("Standard Derivation: ")
    private val humidityValStd = Label("Standard Derivation: ")
    private val pressureValStd = Label("Standard Derivation: ")
    private val oxygenValStd = Label("Standard Derivation: ")
    private val salinityValStd = Label("Standard Derivation: ")
    private val turbidityValStd = Label("Standard Derivation: ")
    private val waterTempValStd = Label("Standard Derivation: ")
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
        updateAllData()
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
        hbox(50.0) {
            padding = insets(50.0, 0.0, 0.0, 5.0)
            alignment = Pos.CENTER
            vbox(30.0) {
                label("Temperature")
                label("Humidity")
                label("Pressure")
                label("Oxygen Content")
                label("Salinity")
                label("Turbidity")
                label("Water Temperature")
            }

            vbox(30.0) {
                add(tempVal)
                add(humidityVal)
                add(pressureVal)
                add(oxygenVal)
                add(salinityVal)
                add(turbidityVal)
                add(waterTempVal)
            }
            vbox(30.0) {
                add(tempValStd)
                add(humidityValStd)
                add(pressureValStd)
                add(oxygenValStd)
                add(salinityValStd)
                add(turbidityValStd)
                add(waterTempValStd)
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
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
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

        tempVal.text = "Average: ${df.format(temperatureValues.averageN())}"
        humidityVal.text = "Average: ${df.format(humidityValues.averageN())}"
        pressureVal.text = "Average: ${df.format(pressureValues.averageN())}"
        oxygenVal.text = "Average: ${df.format(oxygenContentValues.averageN())}"
        salinityVal.text = "Average: ${df.format(salinityValues.averageN())}"
        turbidityVal.text = "Average: ${df.format(turbidityValues.averageN())}"
        waterTempVal.text = "Average: ${df.format(waterTemperatureValues.averageN())}"


        tempValStd.text = "Standard Deviation: ${df.format(temperatureValues.standardDeviation())}"
        humidityValStd.text = "Standard Deviation: ${df.format(humidityValues.standardDeviation())}"
        pressureValStd.text = "Standard Deviation: ${df.format(pressureValues.standardDeviation())}"
        oxygenValStd.text = "Standard Deviation: ${df.format(oxygenContentValues.standardDeviation())}"
        salinityValStd.text = "Standard Deviation: ${df.format(salinityValues.standardDeviation())}"
        turbidityValStd.text = "Standard Deviation: ${df.format(turbidityValues.standardDeviation())}"
        waterTempValStd.text = "Standard Deviation: ${df.format(waterTemperatureValues.standardDeviation())}"
    }

    private fun updateAllData() {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val temperatureValues = sensorDataList.map { it.temperature }
        val humidityValues = sensorDataList.map { it.humidity }
        val pressureValues = sensorDataList.map { it.pressure }
        val oxygenContentValues = sensorDataList.map { it.oxygenContent }
        val salinityValues = sensorDataList.map { it.salinity }
        val turbidityValues = sensorDataList.map { it.turbidity }
        val waterTemperatureValues = sensorDataList.map { it.waterTemperature }

        tempVal.text = "Average: ${df.format(temperatureValues.averageN())}"
        humidityVal.text = "Average: ${df.format(humidityValues.averageN())}"
        pressureVal.text = "Average: ${df.format(pressureValues.averageN())}"
        oxygenVal.text = "Average: ${df.format(oxygenContentValues.averageN())}"
        salinityVal.text = "Average: ${df.format(salinityValues.averageN())}"
        turbidityVal.text = "Average: ${df.format(turbidityValues.averageN())}"
        waterTempVal.text = "Average: ${df.format(waterTemperatureValues.averageN())}"


        tempValStd.text = "Standard Deviation: ${df.format(temperatureValues.standardDeviation())}"
        humidityValStd.text = "Standard Deviation: ${df.format(humidityValues.standardDeviation())}"
        pressureValStd.text = "Standard Deviation: ${df.format(pressureValues.standardDeviation())}"
        oxygenValStd.text = "Standard Deviation: ${df.format(oxygenContentValues.standardDeviation())}"
        salinityValStd.text = "Standard Deviation: ${df.format(salinityValues.standardDeviation())}"
        turbidityValStd.text = "Standard Deviation: ${df.format(turbidityValues.standardDeviation())}"
        waterTempValStd.text = "Standard Deviation: ${df.format(waterTemperatureValues.standardDeviation())}"
    }
}