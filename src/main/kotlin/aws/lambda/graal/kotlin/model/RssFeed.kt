package aws.lambda.graal.kotlin.model

import com.sun.syndication.feed.synd.SyndCategoryImpl
import com.sun.syndication.feed.synd.SyndEntry
import com.sun.syndication.feed.synd.SyndFeed
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class RssFeeds(val feeds: List<RssFeed>)
data class RssFeed(val description: String, val categories: List<String>, val link: String, val publishedDate: LocalDateTime)

fun List<SyndEntry>.toRssFeeds(): RssFeeds {
    val feeds = this.map {
        RssFeed(
                it.description?.value ?: "",
                it.typedCategories().mapNotNull { category -> category.name },
                it.link,
                it.publishedDate.toLocalDateTime()
        )
    }
    return RssFeeds(feeds)
}


internal fun SyndFeed.typedEntries(): List<SyndEntry> =
        (this.entries as List<SyndEntry?>).mapNotNull { it }

private fun SyndEntry.typedCategories(): List<SyndCategoryImpl> =
        (this.categories as List<SyndCategoryImpl?>).mapNotNull { it }

private fun Date?.toLocalDateTime(): LocalDateTime =
        this?.run {
            Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime()
        } ?: LocalDateTime.now()