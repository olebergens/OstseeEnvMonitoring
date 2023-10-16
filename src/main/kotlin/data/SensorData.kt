package data

import java.time.LocalDateTime

data class SensorData(val timestamp: LocalDateTime, val temperature: Int?, val humidity: Int?, val pressure: Int?)