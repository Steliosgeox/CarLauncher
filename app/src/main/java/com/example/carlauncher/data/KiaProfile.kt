package com.example.carlauncher.data

/**
 * KIA Pro_Cee'd ED Facelift 2010
 * 1.4 G4FA Gamma B (109 hp)
 * ECU: Bosch
 * 
 * Standard PIDs generally work for RPM, Speed, Coolant.
 * Custom PIDs below might be required for advanced metrics based on K-Line or CAN.
 */
object KiaProceed2010Profile {
    // Basic OBD-II
    const val PID_RPM = "010C"
    const val PID_SPEED = "010D"
    const val PID_COOLANT_TEMP = "0105"
    const val PID_INTAKE_TEMP = "010F"
    
    // Example Custom KIA PIDs (Often required for Oil Temp or Transmission if supported)
    // Note: Actual hex values depend on the specific Bosch ECU DBC files.
    const val PID_KIA_CUSTOM_OIL_TEMP = "220101" // Example KWP2000/UDS PID
    
    fun calculateOilTemp(hexResponse: String): Int {
        // Example formula: A - 40
        return try {
            val a = hexResponse.substring(hexResponse.length - 2).toInt(16)
            a - 40
        } catch (e: Exception) {
            0
        }
    }
}
