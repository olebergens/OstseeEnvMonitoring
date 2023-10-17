package views

import javafx.scene.control.TabPane
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane

class MainView : View("Baltic Sea Environment Monitoring") {

    override val root = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        tab(SensorDataGraphView::class)
        tab(AverageDataValuesView::class)
        tab(MaxMinValuesView::class)
    }
}