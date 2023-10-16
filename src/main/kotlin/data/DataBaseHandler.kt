package data

class DataReader(private val filePath: String) {
    fun readData(): List<SensorData>
}