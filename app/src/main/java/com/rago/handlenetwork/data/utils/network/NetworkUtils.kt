package com.rago.handlenetwork.data.utils.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.telephony.TelephonyManager
import android.util.Log

object NetworkUtils {

    fun getNetworkRequest(context:Context, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.d("Network", "Conectado a Wi-Fi")
                    }
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.d("Network", "Conectado a datos móviles")
                    }
                }
            }
        }

        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    // Function to find out type of network
    fun mGetNetworkClass(context: Context): String {

        // ConnectionManager instance
        val mConnectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val mInfo = mConnectivityManager.activeNetworkInfo

        // If not connected, "-" will be displayed
        if (mInfo == null || !mInfo.isConnected) return "-"

        // If Connected to Wifi
        if (mInfo.type == ConnectivityManager.TYPE_WIFI) return "WIFI"

        // If Connected to Mobile
        if (mInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return when (mInfo.subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN, TelephonyManager.NETWORK_TYPE_GSM -> "2G"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "3G"
                TelephonyManager.NETWORK_TYPE_LTE, TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> "4G"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                else -> "?"
            }
        }
        return "?"
    }
}