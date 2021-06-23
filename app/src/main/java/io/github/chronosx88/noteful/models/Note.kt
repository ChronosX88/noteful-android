package io.github.chronosx88.noteful.models

import java.io.Serializable

data class Note(
    var title: String,
    var createdAt: Long,
    var modifiedAt: Long,
    val tags: List<String> = listOf(),
    val attachments: List<String> = listOf(),
    var favorite: Boolean,
    var pinned: Boolean,
    val additionalData: Map<String, Any> = mapOf()
): Serializable
