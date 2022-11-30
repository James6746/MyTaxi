package com.mytaxi.mytaxitask.model

data class Trip(
    val departurePoint: String,
    val destination: String,
    val time: String,
    val rideCost: String,
    val rideType: Int
)