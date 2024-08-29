package com.trino.isnetworkavailable

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var networkUtil: NetworkUtil
    private var TAG = "networkCheck"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO Initialize NetworkUtil
        networkUtil = NetworkUtil(this)

        //TODO Set the callback to receive network updates
        //TODO it will continuously tells network status
        networkUtil.setNetworkCallback(object : NetworkCallback{
            override fun onNetworkAvailable() {
                //TODO Handle network available event
                Log.i(TAG, "onNetworkAvailable")
            }

            override fun onNetworkLost() {
                //TODO Handle network lost event
                Log.i(TAG, "onNetworkLost")
            }
        })


        //TODO Check network availability immediately if needed
        if (networkUtil.isNetworkAvailableNow()) {
            Toast.makeText(this, "Network is available", Toast.LENGTH_SHORT).show()

            //TODO Perform a ping operation
            networkUtil.performPing("8.8.8.8") //TODO Example: Ping Google's public DNS
        } else {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show()
        }

        //TODO Set the callback to receive network updates
        networkUtil.setPingCallback(object  : PingCallback{
            override fun onPingSuccess(responseTime: Long) {
                Log.i(TAG, "onPingSuccess: $responseTime")
                lifecycleScope.launch {
                    Toast.makeText(this@MainActivity, "Ping successful, response time: $responseTime ms", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPingFailure(error: String) {
                Log.i(TAG, "onPingSuccess: $error")
                lifecycleScope.launch {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}