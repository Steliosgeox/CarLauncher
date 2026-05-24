package com.steliosgeox.carlauncher.vehicle.profile

data class VehicleProfile(
    val make: String,
    val model: String,
    val year: Int,
    val engine: String,
    val platform: String,
    val fuelType: FuelType,
    val rpmRedline: Int,
    val supportedPids: List<String>
)
