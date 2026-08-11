package com.an.core_project.presentation

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun epochSecondsToDateTime(millisSeconds: Long): LocalDateTime {
    return Instant.ofEpochMilli(millisSeconds)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun formatLastEdited(millisSeconds: Long): String {
    val dateTime = epochSecondsToDateTime(millisSeconds)
    val today = LocalDateTime.now().toLocalDate()

    val formatter = if (dateTime.toLocalDate() == today) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("d MMM, HH:mm")
    }

    return dateTime.format(formatter)
}