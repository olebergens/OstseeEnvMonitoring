package views

import data.DataBaseHandler
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.Label
import tornadofx.View
import tornadofx.datepicker
import java.time.LocalDate

class MaxMinValuesView : View("Min/Max Values"){
    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()

    private val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7) }
    private val toDatepicker = datepicker { value = LocalDate.now() }
    private val showFilteredData = SimpleBooleanProperty(false)

    private val tempMinVal = Label("Minimum: ")
    private val humidityMinVal = Label("Minimum: ")
    private val pressureMinVal = Label("Minimum: ")
    private val oxygenMinVal = Label("Minimum: ")
    private val salinityMinVal = Label("Minimum: ")
    private val turbidityMinVal = Label("Minimum: ")
    private val waterTempMinVal = Label("Minimum: ")


    private val tempMaxVal = Label("Maximum: ")
    private val humidityMaxVal = Label("Maximum: ")
    private val pressureMaxVal = Label("Maximum: ")
    private val oxygenMaxVal = Label("Maximum: ")
    private val salinityMaxVal = Label("Maximum: ")
    private val turbidityMaxVal = Label("Maximum: ")
    private val waterTempMaxVal = Label("Maximum: ")
}