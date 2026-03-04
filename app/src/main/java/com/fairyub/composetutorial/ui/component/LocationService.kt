package com.fairyub.composetutorial.ui.component

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationService(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLastLocation(onResult: (latitude: Double, longitude: Double) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onResult(location.latitude, location.longitude)
            }
        }
    }
}