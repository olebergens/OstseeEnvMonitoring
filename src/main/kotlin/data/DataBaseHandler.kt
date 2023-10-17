package data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class DataBaseHandler(private val dbUrl: String, private val username: String, private val password: String) {
    fun getSensorData(): List<SensorData> {
        val sensorDataList = mutableListOf<SensorData>()
        var connection: Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            connection = DriverManager.getConnection(dbUrl, username, password)
            statement = connection.createStatement()
            resultSet = statement.executeQuery("SELECT * FROM sys.sensor_data")
            while (resultSet.next()) {
                val timeStamp = resultSet.getTimestamp("timestamp").toLocalDateTime()
                val temperature = resultSet.getInt("temperature")
                val humidity = resultSet.getInt("humidity")
                val pressure = resultSet.getInt("pressure")
                val oxygenContent = resultSet.getInt("oxygencontent")
                val salinity = resultSet.getInt("salinity")
                val turbidity = resultSet.getInt("turbidity")
                val watertemperature = resultSet.getInt("watertemperature")
                val sensorData = SensorData(timeStamp, temperature, humidity, pressure, oxygenContent, salinity, turbidity, watertemperature)
                sensorDataList.add(sensorData)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            statement?.close()
            connection?.close()
        }
        return sensorDataList
    }
}