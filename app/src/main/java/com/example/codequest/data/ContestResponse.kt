package com.example.codequest.data

data class ContestResponse(
    val objects: List<Contest>
)

data class Contest(
    val id: Int,
    val event: String,
    val host: String,
    val href: String,
    val start: String,
    val end: String
)
