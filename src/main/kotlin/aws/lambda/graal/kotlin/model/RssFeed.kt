package aws.lambda.graal.kotlin.model

import java.time.LocalDateTime

data class RssFeed(val description: String, val categories: List<String>, val link: String, val publishedDate: LocalDateTime)