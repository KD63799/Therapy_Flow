package com.example.therapy_flow.utils

import java.text.SimpleDateFormat
import java.util.Locale

private val inputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
private val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

fun formatDate(dateString: String): String {
    return inputFormatter.parse(dateString)?.let { date ->
        outputFormatter.format(date)
    } ?: "Invalid date"
}
