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

// MaxMinValuesView erbt TornadoFX View und repräsentiert den Screen der die Minima und Maxima ausgibt
class MaxMinValuesView : View("Min/Max Values") {
    // Sensor-Datenliste aus der Datenbank laden
    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()

    // UI-Elemente und Eigenschaften für die Filterung und Anzeige von Sensor-Daten
    private val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7) }
    private val toDatepicker = datepicker { value = LocalDate.now() }
    private val showFilteredData = SimpleBooleanProperty(false)

    // Labels für die Zeitstempel der Min-Werte der verschiedenen Sensor-Daten
    private val timeTempMinVal = Label("Timestamp: ")
    private val timeHumidityMinVal = Label("Timestamp: ")
    private val timePressureMinVal = Label("Timestamp: ")
    private val timeOxygenMinVal = Label("Timestamp: ")
    private val timeSalinityMinVal = Label("Timestamp: ")
    private val timeTurbidityMinVal = Label("Timestamp: ")
    private val timeWaterTempMinVal = Label("Timestamp: ")

    // Labels für die Min-Werte der verschiedenen Sensor-Daten
    private val tempMinVal = Label("Minimum: ")
    private val humidityMinVal = Label("Minimum: ")
    private val pressureMinVal = Label("Minimum: ")
    private val oxygenMinVal = Label("Minimum: ")
    private val salinityMinVal = Label("Minimum: ")
    private val turbidityMinVal = Label("Minimum: ")
    private val waterTempMinVal = Label("Minimum: ")

    // Labels für die Zeitstempel der Max-Werte der verschiedenen Sensor-Daten
    private val timeTempMaxVal = Label("Timestamp: ")
    private val timeHumidityMaxVal = Label("Timestamp: ")
    private val timePressureMaxVal = Label("Timestamp: ")
    private val timeOxygenMaxVal = Label("Timestamp: ")
    private val timeSalinityMaxVal = Label("Timestamp: ")
    private val timeTurbidityMaxVal = Label("Timestamp: ")
    private val timeWaterTempMaxVal = Label("Timestamp: ")

    // Labels für die Max-Werte der verschiedenen Sensor-Daten
    private val tempMaxVal = Label("Maximum: ")
    private val humidityMaxVal = Label("Maximum: ")
    private val pressureMaxVal = Label("Maximum: ")
    private val oxygenMaxVal = Label("Maximum: ")
    private val salinityMaxVal = Label("Maximum: ")
    private val turbidityMaxVal = Label("Maximum: ")
    private val waterTempMaxVal = Label("Maximum: ")

    // Initialisierungsblock für die Konfiguration von UI-Elementen
    init {
        // Listener für die Aktualisierung des Diagramms, wenn sich etwas ändert
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

    // UI-Komponenten und Layout definieren
    override val root = vbox {
        paddingAll = 20.0
        alignment = Pos.CENTER
        hbox(10.0) {
            alignment = Pos.TOP_CENTER
            label("Values")
            padding = insets(0.0, 0.0, 10.0, 0.0)
        }
        // UI-Elemente für Datumspicker & Filter hinzufügen
        hbox(10.0) {
            alignment = Pos.CENTER
            label("From Date:")
            add(fromDatepicker)
            label("To Date:")
            add(toDatepicker)
            checkbox("Filter", showFilteredData)
        }

        // UI-Elemente für Sensor-Datenanzeige hinzufügen
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
                padding = insets(0.0, 0.0, 0.0, -10.0)
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
                padding = insets(0.0, 0.0, 0.0, -10.0)
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

    /**
     * TODO: BEARBEITE DOPPELTE AUFRUFE -> FUNKTIONSERSTELLUNG UND DARÜBER LAUFEN LASSEN
     */

    // Funktion zum Aktualisieren der angezeigten Min/Max-Werte basierend auf dem Zeitraum
    private fun updateFilteredData() {
        // Decimal Formatter für die Rundung auf zwei Nachkommastellen
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        // Daten filtern basierend auf dem ausgewählten Zeitraum
        val filteredData = sensorDataList.filter { it.timestamp in fromDatepicker.value.atStartOfDay()..toDatepicker.value.atTime(LocalTime.MAX) }

        tempMinVal.text = "Minimum: ${filteredData.minByOrNull { it.temperature!! }!!.temperature}"
        tempMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.temperature!! }!!.temperature}"
        timeTempMinVal.text = "at: ${filteredData.minByOrNull { it.temperature!! }!!.timestamp}"
        timeTempMaxVal.text = "at: ${filteredData.maxByOrNull { it.temperature!! }!!.timestamp}"

        humidityMinVal.text = "Minimum: ${filteredData.minByOrNull { it.humidity!! }!!.humidity}"
        humidityMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.humidity!! }!!.humidity}"
        timeHumidityMinVal.text = "at: ${filteredData.minByOrNull { it.humidity!! }!!.timestamp}"
        timeHumidityMaxVal.text = "at: ${filteredData.maxByOrNull { it.humidity!! }!!.timestamp}"

        pressureMinVal.text = "Minimum: ${filteredData.minByOrNull { it.pressure!! }!!.pressure}"
        pressureMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.pressure!! }!!.pressure}"
        timePressureMinVal.text = "at: ${filteredData.minByOrNull { it.pressure!! }!!.timestamp}"
        timePressureMaxVal.text = "at: ${filteredData.maxByOrNull { it.pressure!! }!!.timestamp}"

        oxygenMinVal.text = "Minimum: ${filteredData.minByOrNull { it.oxygenContent!! }!!.oxygenContent}"
        oxygenMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.oxygenContent!! }!!.oxygenContent}"
        timeOxygenMinVal.text = "at: ${filteredData.minByOrNull { it.oxygenContent!! }!!.timestamp}"
        timeOxygenMaxVal.text = "at: ${filteredData.maxByOrNull { it.oxygenContent!! }!!.timestamp}"

        salinityMinVal.text = "Minimum: ${filteredData.minByOrNull { it.salinity!! }!!.salinity}"
        salinityMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.salinity!! }!!.salinity}"
        timeSalinityMinVal.text = "at: ${filteredData.minByOrNull { it.salinity!! }!!.timestamp}"
        timeSalinityMaxVal.text = "at: ${filteredData.maxByOrNull { it.salinity!! }!!.timestamp}"

        turbidityMinVal.text = "Minimum: ${filteredData.minByOrNull { it.turbidity!! }!!.turbidity}"
        turbidityMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.turbidity!! }!!.turbidity}"
        timeTurbidityMinVal.text = "at: ${filteredData.minByOrNull { it.turbidity!! }!!.timestamp}"
        timeTurbidityMaxVal.text = "at: ${filteredData.maxByOrNull { it.turbidity!! }!!.timestamp}"

        waterTempMinVal.text = "Minimum: ${filteredData.minByOrNull { it.waterTemperature!! }!!.waterTemperature}"
        waterTempMaxVal.text = "Maximum: ${filteredData.maxByOrNull { it.waterTemperature!! }!!.waterTemperature}"
        timeWaterTempMinVal.text = "at: ${filteredData.minByOrNull { it.waterTemperature!! }!!.timestamp}"
        timeWaterTempMaxVal.text = "at: ${filteredData.maxByOrNull { it.waterTemperature!! }!!.timestamp}"
    }

    // Funktion zum Aktualisieren der angezeigten Min/Max-Werte basierend auf dem Zeitraum
    private fun updateAllData() {
        // Decimal Formatter für die Rundung auf zwei Nachkommastellen
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        tempMinVal.text = "Minimum: ${sensorDataList.minByOrNull { it.temperature!! }!!.temperature}"
        tempMaxVal.text = "Maximum: ${sensorDataList.maxByOrNull { it.temperature!! }!!.temperature}"
        timeTempMinVal.text = "at: ${sensorDataList.minByOrNull { it.temperature!! }!!.timestamp}"
        timeTempMaxVal.text = "at: ${sensorDataList.maxByOrNull { it.temperature!! }!!.timestamp}"

        humidityMinVal.text = "Minimum: ${sensorDataList.minByOrNull { it.humidity!! }!!.humidity}"
        humidityMaxVal.text = "Maximum: ${sensorDataList.maxByOrNull { it.humidity!! }!!.humidity}"
        timeHumidityMinVal.text = "at: ${sensorDataList.minByOrNull { it.humidity!! }!!.timestamp}"
        timeHumidityMaxVal.text = "at: ${sensorDataList.maxByOrNull { it.humidity!! }!!.timestamp}"

        pressureMinVal.text = "Minimum: ${sensorDataList.minByOrNull { it.pressure!! }!!.pressure}"
        pressureMaxVal.text = "Maximum: ${sensorDataList.maxByOrNull { it.pressure!! }!!.pressure}"
        timePressureMinVal.text = "at: ${sensorDataList.minByOrNull { it.pressure!! }!!.timestamp}"
        timePressureMaxVal.text = "at: ${sensorDataList.maxByOrNull { it.pressure!! }!!.timestamp}"

        oxygenMinVal.text = "Minimum: ${sensorDataList.minByOrNull { it.oxygenContent!! }!!.oxygenContent}"
        oxygenMaxVal.text = "Maximum: ${sensorDataList.maxByOrNull { it.oxygenContent!! }!!.oxygenContent}"
        timeOxygenMinVal.text = "at: ${sensorDataList.minByOrNull { it.oxygenContent!! }!!.timestamp}"
        timeOxygenMaxVal.text = "at: ${sensorDataList.maxByOrNull { it.oxygenContent!! }!!.timestamp}"

        salinityMinVal.text = "Minimum: ${sensorDataList.minByOrNull { it.salinity!! }!!.salinity}"
        salinityMaxVal.text = "Maximum: ${sensorDataList.maxByOrNull { it.salinity!! }!!.salinity}"
        timeSalinityMinVal.text = "at: ${sensorDataList.minByOrNull { it.salinity!! }!!.timestamp}"
        timeSalinityMaxVal.text = "at: ${sensorDataList.maxByOrNull { it.salinity!! }!!.timestamp}"

        turbidityMinVal.text = "Minimum: ${sensorDataList.minByOrNull { it.turbidity!! }!!.turbidity}"
        turbidityMaxVal.text = "Maximum: ${sensorDataList.maxByOrNull { it.turbidity!! }!!.turbidity}"
        timeTurbidityMinVal.text = "at: ${sensorDataList.minByOrNull { it.turbidity!! }!!.timestamp}"
        timeTurbidityMaxVal.text = "at: ${sensorDataList.maxByOrNull { it.turbidity!! }!!.timestamp}"

        waterTempMinVal.text = "Minimum: ${sensorDataList.minByOrNull { it.waterTemperature!! }!!.waterTemperature}"
        waterTempMaxVal.text = "Maximum: ${sensorDataList.maxByOrNull { it.waterTemperature!! }!!.waterTemperature}"
        timeWaterTempMinVal.text = "at: ${sensorDataList.minByOrNull { it.waterTemperature!! }!!.timestamp}"
        timeWaterTempMaxVal.text = "at: ${sensorDataList.maxByOrNull { it.waterTemperature!! }!!.timestamp}"

    }
}