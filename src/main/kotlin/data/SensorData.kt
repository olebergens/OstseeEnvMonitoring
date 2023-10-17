package data

import java.time.LocalDateTime

/**
 * Datenklasse zur Repräsentation von Sensordaten
 *
 * @property timestamp Das Datum und die Uhrzeit, zu der die Daten erfasst wurden
 * @property temperature Die gemessene Lufttemperatur in Grad Celsius (°C). Kann null sein, wenn die Temperatur nicht gemessen wurde
 * @property humidity Die gemessene Luftfeuchtigkeit in Prozent (%). Kann null sein, wenn die Luftfeuchtigkeit nicht gemessen wurde
 * @property pressure Der gemessene Luftdruck in Hektopascal (hPa). Kann null sein, wenn der Luftdruck nicht gemessen wurde
 * @property oxygenContent Der gemessene Sauerstoffgehalt in Prozent (%). Kann null sein, wenn der Sauerstoffgehalt nicht gemessen wurde
 * @property salinity Der gemessene Salzgehalt in Teilen pro Tausend (ppt). Kann null sein, wenn der Salzgehalt nicht gemessen wurde
 * @property turbidity Der gemessene Trübungswert. Kann null sein, wenn der Trübungswert nicht gemessen wurde
 * @property waterTemperature Die gemessene Wassertemperatur in Grad Celsius (°C). Kann null sein, wenn die Wassertemperatur nicht gemessen wurde.
 */
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