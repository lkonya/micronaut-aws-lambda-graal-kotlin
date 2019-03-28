package aws.lambda.graal.kotlin.controller

import aws.lambda.graal.kotlin.client.RssFeedClient
import com.sun.syndication.feed.synd.SyndEntryImpl
import com.sun.syndication.feed.synd.SyndFeed
import io.micronaut.http.MediaType.TEXT_XML
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable

@Controller("/rss")
class RssController(private val feedClient: RssFeedClient) {

    @Get("/feed2")
    fun feed(): Flowable<List<String>> = feedClient.fetchRssFromRaw().map {
        it.typedEntries().mapNotNull { entry -> entry.title }
    }

    @Get(value = "/raw", produces = [TEXT_XML])
    fun raw(): Flowable<String> = feedClient.fetchRawRss()

    @Get("/feed")
    fun asyncFeed(): Flowable<SyndFeed> = feedClient.fetchRss()

    private fun SyndFeed.typedEntries(): List<SyndEntryImpl> =
            this.entries as List<SyndEntryImpl>
}