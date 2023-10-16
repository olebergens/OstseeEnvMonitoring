import tornadofx.App
import tornadofx.launch
import views.MainView

class OstseeEnvMonitoring : App(MainView::class)

fun main() {
    launch<OstseeEnvMonitoring>()
}