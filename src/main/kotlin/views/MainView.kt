package views

import javafx.scene.control.TabPane
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane

// Hauptansicht der Anwendung, die Tabs für verschiedene Funktion enthält
class MainView : View("Baltic Sea Environment Monitoring") {

    // Wurzel-Container für die TabPane-Ansicht
    override val root = tabpane {
        // Tabs können nicht mehr geschlossen werden
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        // Tabs für verschiedene Funktionen hinzufügen
        tab(SensorDataGraphView::class)
        tab(AverageDataValuesView::class)
        tab(MaxMinValuesView::class)
    }
}