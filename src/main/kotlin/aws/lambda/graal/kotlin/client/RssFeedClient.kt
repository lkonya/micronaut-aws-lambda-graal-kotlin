package aws.lambda.graal.kotlin.client

import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader
import io.micronaut.context.annotation.Property
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.reactivex.Flowable
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RssFeedClient(@Client("\${rss.baseurl}") @Inject private val httpclient: RxHttpClient,
                    @Property(name = "rss.path") private val path: String) {

    fun fetchRssFromRaw(): Flowable<SyndFeed> =
            fetchRawRss().map { createFeed(it.byteInputStream()) }

    fun fetchRss(): Flowable<SyndFeed> =
            httpclient.exchange(path).map { createFeed(it.body()?.toInputStream()) }

    fun fetchRawRss(): Flowable<String> =
            httpclient.exchange(path, String::class.java).map { it.body() }

    private fun createFeed(inputStream: InputStream?): SyndFeed =
            SyndFeedInput().build(XmlReader(inputStream))

}
