package views

import com.jfoenix.controls.JFXCheckBox
import data.DataBaseHandler
import data.SensorData
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import tornadofx.*
import java.time.LocalDate
import java.time.LocalDateTime

class SensorDataGraphView : View("Data Graph") {

    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "Blubber02!").getSensorData()
    private val fromDatepicker = datepicker { value = LocalDate.now().minusDays(7) }
    private val toDatepicker = datepicker { value = LocalDate.now() }
    private val showFilteredData = SimpleBooleanProperty(false)

    private val showTemperature = SimpleBooleanProperty(false)
    private val showHumidity = SimpleBooleanProperty(false)
    private val showPressure = SimpleBooleanProperty(false)
    private val showOxygenContent = SimpleBooleanProperty(false)
    private val showSalinity = SimpleBooleanProperty(false)
    private val showTurbidity = SimpleBooleanProperty(false)
    private val showWaterTemperature = SimpleBooleanProperty(false)

    private val temperatureSeries = XYChart.Series<String, Number>().apply { name = "Temperature" }
    private val humiditySeries = XYChart.Series<String, Number>().apply { name = "Humidity" }
    private val pressureSeries = XYChart.Series<String, Number>().apply { name = "Pressure" }
    private val oxygenContentSeries = XYChart.Series<String, Number>().apply { name = "Oxygen Content" }
    private val salinitySeries = XYChart.Series<String, Number>().apply { name = "Salinity" }
    private val turbiditySeries = XYChart.Series<String, Number>().apply { name = "Turbidity" }
    private val waterTemperatureSeries = XYChart.Series<String, Number>().apply { name = "Water Temperature" }

    init {
        importStylesheet(stylesheet = "/style.css")
        showTemperature.addListener { _, _, _ -> updateChart() }
        showHumidity.addListener { _, _, _ -> updateChart() }
        showPressure.addListener { _, _, _ -> updateChart() }
        showOxygenContent.addListener { _, _, _ -> updateChart() }
        showSalinity.addListener { _, _, _ -> updateChart() }
        showTurbidity.addListener { _, _, _ -> updateChart() }
        showWaterTemperature.addListener { _, _, _ -> updateChart() }

        fromDatepicker.valueProperty().addListener { _, _, _ -> if (showFilteredData.get()) updateChart() }
        toDatepicker.valueProperty().addListener { _, _, _ -> if (showFilteredData.get()) updateChart() }
    }


    override val root = vbox {
        prefWidth = 800.0
        prefHeight = 500.0
        addClass("root")
        paddingAll = 20.0
        alignment = Pos.CENTER

        hbox(10.0) {
            alignment = Pos.CENTER
            label("From Date:")
            add(fromDatepicker)
            label("To Date:")
            add(toDatepicker)
            add(JFXCheckBox("Filter").apply {
                selectedProperty().bindBidirectional(showFilteredData)
            })
        }

        hbox(15.0) {
            alignment = Pos.CENTER
            padding = insets(10.0, 0.0, 0.0, 0.0)
            add(JFXCheckBox("Temperature").apply {
                styleClass.add("checkbox")
                selectedProperty().bindBidirectional(showTemperature)
            })
            add(JFXCheckBox("Humidity").apply {
                styleClass.add("checkbox")
                selectedProperty().bindBidirectional(showHumidity)
            })
            add(JFXCheckBox("Pressure").apply {
                styleClass.add("checkbox")
                selectedProperty().bindBidirectional(showPressure)
            })
            add(JFXCheckBox("Oxygen Content").apply {
                styleClass.add("checkbox")
                selectedProperty().bindBidirectional(showOxygenContent)
            })
            add(JFXCheckBox("Salinity").apply {
                styleClass.add("checkbox")
                selectedProperty().bindBidirectional(showSalinity)
            })
            add(JFXCheckBox("Turbidity").apply {
                styleClass.add("checkbox")
                selectedProperty().bindBidirectional(showTurbidity)
            })
            add(JFXCheckBox("Water Temperature").apply {
                styleClass.add("checkbox")
                selectedProperty().bindBidirectional(showWaterTemperature)
            })
        }

        vbox {
            padding = insets(10.0, 0.0, 0.0, 0.0)
            linechart("Data Graph", CategoryAxis(), NumberAxis()) {
                addClass("chart-legend")
                data = FXCollections.observableArrayList(
                    temperatureSeries,
                    humiditySeries,
                    pressureSeries,
                    oxygenContentSeries,
                    salinitySeries,
                    turbiditySeries,
                    waterTemperatureSeries)
                updateChart()
            }
        }
        primaryStage.isResizable = false
    }


    private fun updateChart() {
        temperatureSeries.data.clear()
        humiditySeries.data.clear()
        pressureSeries.data.clear()
        oxygenContentSeries.data.clear()
        salinitySeries.data.clear()
        turbiditySeries.data.clear()
        waterTemperatureSeries.data.clear()

        sensorDataList.forEach { sensorData ->
            val localDateTime = sensorData.timestamp
            if (isDataValid(sensorData) && isDateInRange(localDateTime)) {
                if (showTemperature.value && sensorData.temperature != null) {
                    temperatureSeries.data.add(XYChart.Data(localDateTime.toString(), sensorData.temperature.toDouble()))
                }
                if (showHumidity.value && sensorData.humidity != null) {
                    humiditySeries.data.add(XYChart.Data(localDateTime.toString(), sensorData.humidity.toDouble()))
                }
                if (showPressure.value && sensorData.pressure != null) {
                    pressureSeries.data.add(XYChart.Data(localDateTime.toString(), sensorData.pressure.toDouble()))
                }
                if (showOxygenContent.value && sensorData.oxygenContent != null) {
                    oxygenContentSeries.data.add(XYChart.Data(localDateTime.toString(), sensorData.oxygenContent.toDouble()))
                }
                if (showSalinity.value && sensorData.salinity != null) {
                    salinitySeries.data.add(XYChart.Data(localDateTime.toString(), sensorData.salinity.toDouble()))
                }
                if (showTurbidity.value && sensorData.turbidity != null) {
                    turbiditySeries.data.add(XYChart.Data(localDateTime.toString(), sensorData.turbidity.toDouble()))
                }
                if (showWaterTemperature.value && sensorData.waterTemperature != null) {
                    waterTemperatureSeries.data.add(XYChart.Data(localDateTime.toString(), sensorData.waterTemperature.toDouble()))
                }
            }
        }

        temperatureSeries.node.isVisible = showTemperature.value
        humiditySeries.node.isVisible = showHumidity.value
        pressureSeries.node.isVisible = showPressure.value
        oxygenContentSeries.node.isVisible = showOxygenContent.value
        salinitySeries.node.isVisible = showSalinity.value
        turbiditySeries.node.isVisible = showTurbidity.value
        waterTemperatureSeries.node.isVisible = showWaterTemperature.value
    }
    private fun isDataValid(sensorData: SensorData): Boolean {
        return (sensorData.temperature != null || sensorData.humidity != null || sensorData.pressure != null)
    }

    private fun isDateInRange(localDateTime: LocalDateTime): Boolean {
        val startDate = fromDatepicker.value.atStartOfDay()
        val endDate = toDatepicker.value.atStartOfDay().plusDays(1)
        return localDateTime in startDate..endDate
    }
}
