package com.trino.isnetworkavailable

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import java.io.IOException
import java.net.InetAddress

/**
 * Utility class for monitoring network connectivity.
 *
 * @param context The application context.
 * 8/29/2024
 * @author Musab Umair
 */

class NetworkUtil(private val context: Context) {

    //TODO LiveData to observe network connectivity changes
    private var networkCallback: NetworkCallback? = null
    private var pingCallback: PingCallback? = null

    init {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                networkCallback?.onNetworkAvailable()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                networkCallback?.onNetworkLost()
            }
        })
    }

    /**
     * Sets the callback to receive network connectivity updates.
     * It will continuously updates the status.
     * @param callback The callback to set.
     */
    fun setNetworkCallback(callback: NetworkCallback) {
        networkCallback = callback
    }

    /**
     * Sets the callback to receive ping status updates.
     *
     * @param callback The callback to set.
     */
    fun setPingCallback(callback: PingCallback) {
        pingCallback = callback
    }

    /**
     * Checks the current network availability.
     *
     * @return True if the network is available, false otherwise.
     */

    fun isNetworkAvailableNow(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected == true
        }
    }


    /**
     * Performs a ping operation to check network latency.
     *
     * @param host The host to ping.
     */
    fun performPing(host: String) {
        Thread {
            try {
                val inetAddress = InetAddress.getByName(host)
                val startTime = System.currentTimeMillis()
                val reachable = inetAddress.isReachable(5000) // Timeout in milliseconds
                val endTime = System.currentTimeMillis()

                if (reachable) {
                    val responseTime = endTime - startTime
                    pingCallback?.onPingSuccess(responseTime)
                } else {
                    pingCallback?.onPingFailure("Ping failed: Host not reachable")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                pingCallback?.onPingFailure(e.message ?: "Ping failed due to an unknown error")
            }
        }.start()
    }
}