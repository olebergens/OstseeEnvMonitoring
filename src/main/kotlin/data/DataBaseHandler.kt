package data

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

/**
 * Behandelt die Datenbankoperationen im Zusammenhang mit den Sensordaten
 * @property dbUrl Die URL der Datenbank
 * @property username Der Benutzername für die Datenbankauthentifizierung
 * @property password Das Passwort für die Datenbankauthentifizierung
 */
class DataBaseHandler(private val dbUrl: String, private val username: String, private val password: String) {
    /**
     * Ruft eine Liste von SensorData-Objekten aus der Datenbank ab
     * @return Eine Liste von SensorData-Objekten, die Sensormessungen darstellen
     */
    fun getSensorData(): List<SensorData> {
        val sensorDataList = mutableListOf<SensorData>()
        var connection: Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            // Datenbankverbindung herstellen
            connection = DriverManager.getConnection(dbUrl, username, password)
            statement = connection.createStatement()
            // Verwende parameterisierte Abfragen, um SQL-Injektion zu verhindern
            val query = "SELECT * FROM sys.sensor_data"
            resultSet = statement.executeQuery(query)
            // Sensordaten aus dem Resultset abrufen und die Liste auffüllen
            while (resultSet.next()) {
                val timeStamp = resultSet.getTimestamp("timestamp").toLocalDateTime()
                val temperature = resultSet.getInt("temperature")
                val humidity = resultSet.getInt("humidity")
                val pressure = resultSet.getInt("pressure")
                val oxygenContent = resultSet.getInt("oxygencontent")
                val salinity = resultSet.getInt("salinity")
                val turbidity = resultSet.getInt("turbidity")
                val watertemperature = resultSet.getInt("watertemperature")
                // Sensordaten erstellen und der Liste hinzufügen
                val sensorData = SensorData(timeStamp, temperature, humidity, pressure, oxygenContent, salinity, turbidity, watertemperature)
                sensorDataList.add(sensorData)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            // Ressourcen in umgekehrter Reihenfolge ihrer Erstellung schließen, um Lecks zu vermeiden
            resultSet?.close()
            statement?.close()
            connection?.close()
        }
        return sensorDataList
    }
}