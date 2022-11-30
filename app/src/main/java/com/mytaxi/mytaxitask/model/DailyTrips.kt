package com.mytaxi.mytaxitask.model

data class DailyTrips(
    val date: String,
    val trips: List<Trip>
)