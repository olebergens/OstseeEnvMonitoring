package data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataBaseHandler(private val dbUrl: String, private val username: String, private val password: String) {
    fun getSensorData(): List<SensorData> {
        val sensorDataList = mutableListOf<SensorData>()
        var connection: Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            connection = DriverManager.getConnection(dbUrl, username, password)
            statement = connection.createStatement()
            resultSet = statement.executeQuery("SELECT * FROM ostseeenvmonitoring.sensor_data")
            while (resultSet.next()) {
                val timeStamp = resultSet.getTimestamp("timestamp").toLocalDateTime()
                val temperature = resultSet.getInt("temperature")
                val humidty = resultSet.getInt("humidity")
                val pressure = resultSet.getInt("pressure")
                val sensorData = SensorData(timeStamp, temperature, humidty, pressure)
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