package aws.lambda.graal.kotlin.controller

import aws.lambda.graal.kotlin.client.RssFeedClient
import aws.lambda.graal.kotlin.model.RssFeeds
import aws.lambda.graal.kotlin.model.toRssFeeds
import aws.lambda.graal.kotlin.model.typedEntries
import io.micronaut.http.MediaType.TEXT_XML
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable

@Controller("/rss")
class RssController(private val feedClient: RssFeedClient) {


    @Get("/feed")
    fun feed(): Flowable<RssFeeds> =
            feedClient.fetchRssFromRaw()
                    .map { it.typedEntries().toRssFeeds() }

    @Get(value = "/raw", produces = [TEXT_XML])
    fun raw(): Flowable<String> = feedClient.fetchRawRss()

}