package views

import data.DataBaseHandler
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import tornadofx.*
import java.time.LocalDate
import kotlin.math.sqrt

class AverageDataValuesView : View("Average Values") {

    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()

    private val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7) }
    private val toDatepicker = datepicker { value = LocalDate.now() }
    private val showFilteredData = SimpleBooleanProperty(false)


    init {
       // fromDatepicker.valueProperty().addListener { _, _, _ -> if (showFilteredData.get()) update() }
        //toDatepicker.valueProperty().addListener { _, _, _ -> if (showFilteredData.get()) update() }

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
        }
        vbox(30.0) {
            padding = insets(50.0, 0.0, 0.0, 5.0)
            alignment = Pos.CENTER_LEFT
            hbox(50.0){
                label("Temperature")

            }
            hbox(50.0) {
                label("Humidity")
                label("Timestamp: ")
                label("Value: ")
            }
            hbox(50.0) {
                label("Pressure")
                label("Timestamp: ")
                label("Value: ")
            }
            hbox(50.0) {
                label("Oxygen Content")
                label("Timestamp: ")
                label("Value: ")
            }
            hbox(50.0) {
                label("Salinity")
                label("Timestamp: ")
                label("Value: ")
            }
            hbox(50.0) {
                label("Turbidity")
                label("Timestamp: ")
                label("Value: ")
            }
            hbox(50.0) {
                label("Water Temperature")
                label("Timestamp: ")
                label("Value: ")
            }
        }
    }

    private fun List<Double>.averageN(): Double {
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