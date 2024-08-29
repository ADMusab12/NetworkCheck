package com.trino.isnetworkavailable

/**
 * Interface to receive network availability updates.
 */
interface NetworkCallback {
    /**
     * Called when network connectivity is available.
     */
    fun onNetworkAvailable()

    /**
     * Called when network connectivity is lost.
     */
    fun onNetworkLost()
}
