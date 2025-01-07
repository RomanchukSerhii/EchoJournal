package com.serhiiromanchuk.echojournal.domain.entity

import com.serhiiromanchuk.echojournal.utils.Constants

data class Topic(
    val id: Long = Constants.INITIAL_TOPIC_ID,
    val name: String
)