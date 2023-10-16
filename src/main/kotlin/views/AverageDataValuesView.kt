package views

import data.DataBaseHandler
import javafx.geometry.Pos
import tornadofx.*
import java.time.LocalDate

class AverageDataValuesView : View("Average Values") {

    private val sensorDataList = DataBaseHandler("jdbc:mysql://localhost:3306", "root", "root").getSensorData()

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
                    a
                }
            }
        }
    }
}