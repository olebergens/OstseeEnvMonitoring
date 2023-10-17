package data

import java.time.LocalDateTime

data class SensorData
    (
    val timestamp: LocalDateTime,
    val temperature: Int?,
    val humidity: Int?,
    val pressure: Int?,
    val oxygenContent: Int?,
    val salinity: Int?,
    val turbidity: Int?,
    val waterTemperature: Int?,
)

data class DataWithTimestamp(val timestamp: LocalDateTime, val value: Double)