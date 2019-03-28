package aws.lambda.graal.kotlin.controller

import aws.lambda.graal.kotlin.client.RssFeedClient
import aws.lambda.graal.kotlin.model.RssFeed
import com.sun.syndication.feed.synd.SyndCategoryImpl
import com.sun.syndication.feed.synd.SyndEntryImpl
import com.sun.syndication.feed.synd.SyndFeed
import io.micronaut.http.MediaType.TEXT_XML
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Controller("/rss")
class RssController(private val feedClient: RssFeedClient) {

    @Get("/feed")
    fun feed(): Flowable<List<RssFeed>> = feedClient.fetchRssFromRaw().map { toRssFeed(it) }

    @Get(value = "/raw", produces = [TEXT_XML])
    fun raw(): Flowable<String> = feedClient.fetchRawRss()

    private fun toRssFeed(feed: SyndFeed): List<RssFeed> {
        return feed.typedEntries().mapNotNull { it }
                .map {
                    RssFeed(
                            it.description?.value ?: "",
                            it.typedCategories().mapNotNull { category -> category?.name },
                            it.link,
                            it.publishedDate.toLocalDateTime()
                    )
                }
    }

    private fun SyndFeed.typedEntries(): List<SyndEntryImpl?> =
            this.entries as List<SyndEntryImpl?>

    private fun SyndEntryImpl.typedCategories(): List<SyndCategoryImpl?> =
            this.categories as List<SyndCategoryImpl?>

    private fun Date?.toLocalDateTime(): LocalDateTime =
            this?.run {
                Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime()
            } ?: LocalDateTime.now()
}