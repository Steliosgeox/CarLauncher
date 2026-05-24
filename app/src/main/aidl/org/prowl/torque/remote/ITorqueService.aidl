package org.prowl.torque.remote;

interface ITorqueService {
    /**
     * Get the version of the API
     */
    int getVersion();

    /**
     * Read a specific PID value
     * PID is in hex without '01' prefix (e.g. "0C" for RPM)
     */
    float getValueForPid(String pid, boolean activeOnly);

    /**
     * Returns true if Torque is currently connected to the ECU
     */
    boolean isConnectedToECU();

    /**
     * Returns a string array of active PIDs (hex strings)
     */
    String[] getActivePIDs();
}
