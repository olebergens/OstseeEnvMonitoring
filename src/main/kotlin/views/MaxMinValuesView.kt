package views

import data.DataBaseHandler
import data.SensorData
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import tornadofx.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.sqrt

class MaxMinValuesView : View("Min/Max Values") {
    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()

    private val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7) }
    private val toDatepicker = datepicker { value = LocalDate.now() }
    private val showFilteredData = SimpleBooleanProperty(false)

    private val timeTempMinVal = Label("Timestamp: ")
    private val timeHumidityMinVal = Label("Timestamp: ")
    private val timePressureMinVal = Label("Timestamp: ")
    private val timeOxygenMinVal = Label("Timestamp: ")
    private val timeSalinityMinVal = Label("Timestamp: ")
    private val timeTurbidityMinVal = Label("Timestamp: ")
    private val timeWaterTempMinVal = Label("Timestamp: ")

    private val tempMinVal = Label("Minimum: ")
    private val humidityMinVal = Label("Minimum: ")
    private val pressureMinVal = Label("Minimum: ")
    private val oxygenMinVal = Label("Minimum: ")
    private val salinityMinVal = Label("Minimum: ")
    private val turbidityMinVal = Label("Minimum: ")
    private val waterTempMinVal = Label("Minimum: ")


    private val timeTempMaxVal = Label("Timestamp: ")
    private val timeHumidityMaxVal = Label("Timestamp: ")
    private val timePressureMaxVal = Label("Timestamp: ")
    private val timeOxygenMaxVal = Label("Timestamp: ")
    private val timeSalinityMaxVal = Label("Timestamp: ")
    private val timeTurbidityMaxVal = Label("Timestamp: ")
    private val timeWaterTempMaxVal = Label("Timestamp: ")

    private val tempMaxVal = Label("Maximum: ")
    private val humidityMaxVal = Label("Maximum: ")
    private val pressureMaxVal = Label("Maximum: ")
    private val oxygenMaxVal = Label("Maximum: ")
    private val salinityMaxVal = Label("Maximum: ")
    private val turbidityMaxVal = Label("Maximum: ")
    private val waterTempMaxVal = Label("Maximum: ")

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
                add(tempMinVal)
                add(humidityMinVal)
                add(pressureMinVal)
                add(oxygenMinVal)
                add(salinityMinVal)
                add(turbidityMinVal)
                add(waterTempMinVal)
            }
            vbox(30.0) {
                add(timeTempMinVal)
                add(timeHumidityMinVal)
                add(timePressureMinVal)
                add(timeOxygenMinVal)
                add(timeSalinityMinVal)
                add(timeTurbidityMinVal)
                add(timeWaterTempMinVal)
            }
            vbox(30.0) {
                add(tempMaxVal)
                add(humidityMaxVal)
                add(pressureMaxVal)
                add(oxygenMaxVal)
                add(salinityMaxVal)
                add(turbidityMaxVal)
                add(waterTempMaxVal)
            }
            vbox(30.0) {
                add(timeTempMaxVal)
                add(timeHumidityMaxVal)
                add(timePressureMaxVal)
                add(timeOxygenMaxVal)
                add(timeSalinityMaxVal)
                add(timeTurbidityMaxVal)
                add(timeWaterTempMaxVal)
            }
        }
    }

    private fun updateFilteredData() {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val filteredData = sensorDataList.filter { it.timestamp in fromDatepicker.value.atStartOfDay()..toDatepicker.value.atTime(LocalTime.MAX) }
        tempMinVal.text = "Minimum: ${filteredData.minByOrNull { it.temperature!! }}"
        tempMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.temperature!! }}"

        humidityMinVal.text = "Minimum: ${filteredData.minByOrNull { it.humidity!! }}"
        humidityMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.humidity!! }}"

        pressureMinVal.text = "Minimum: ${filteredData.minByOrNull { it.pressure!! }}"
        pressureMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.pressure!! }}"

        oxygenMinVal.text = "Minimum: ${filteredData.minByOrNull { it.oxygenContent!! }}"
        oxygenMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.oxygenContent!! }}"

        salinityMinVal.text = "Minimum: ${filteredData.minByOrNull { it.salinity!! }}"
        salinityMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.salinity!! }}"

        turbidityMinVal.text = "Minimum: ${filteredData.minByOrNull { it.turbidity!! }}"
        turbidityMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.turbidity!! }}"

        waterTempMinVal.text = "Minimum: ${filteredData.minByOrNull { it.waterTemperature!! }}"
        waterTempMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.waterTemperature!! }}"
    }

    private fun updateAllData() {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val filteredData = sensorDataList.filter { it.timestamp in fromDatepicker.value.atStartOfDay()..toDatepicker.value.atTime(LocalTime.MAX) }
        val temperatureValues = filteredData.map { it.temperature }
        val humidityValues = filteredData.map { it.humidity }
        val pressureValues = filteredData.map { it.pressure }
        val oxygenContentValues = filteredData.map { it.oxygenContent }
        val salinityValues = filteredData.map { it.salinity }
        val turbidityValues = filteredData.map { it.turbidity }
        val waterTemperatureValues = filteredData.map { it.waterTemperature }
    }
}