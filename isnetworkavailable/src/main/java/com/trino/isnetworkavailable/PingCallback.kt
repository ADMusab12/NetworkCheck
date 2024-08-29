package com.trino.isnetworkavailable

/**
 * Interface to receive ping status updates.
 */
interface PingCallback {
    /**
     * Called when the ping operation is successful.
     *
     * @param responseTime The response time of the ping in milliseconds.
     */
    fun onPingSuccess(responseTime: Long)

    /**
     * Called when the ping operation fails.
     */
    fun onPingFailure(s: String)
}
