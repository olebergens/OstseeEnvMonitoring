package views

import javafx.scene.control.TabPane
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane

class MainView : View("Ostsee Environment Monitoring") {

    override val root = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        tab(SensorDataGraphView::class)
        tab(AverageDataValuesView::class)
    }
}