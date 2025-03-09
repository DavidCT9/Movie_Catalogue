package com.example.practice2_3

data class Movie(
    val name: String = "",
    val year: String = "",
    val genre: String = "",
    val latitude: Double = 0.0,  // Must be Double
    val longitude: Double = 0.0   // Must be Double
) {
    constructor() : this("", "", "", 0.0, 0.0) // Required for Firebase
}