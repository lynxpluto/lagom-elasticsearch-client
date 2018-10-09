package org.taymyr.lagom.elasticsearch.search

import akka.util.ByteString
import com.lightbend.lagom.javadsl.api.Descriptor
import com.lightbend.lagom.javadsl.api.Service
import com.lightbend.lagom.javadsl.api.Service.named
import com.lightbend.lagom.javadsl.api.Service.restCall
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.api.transport.Method.GET
import org.taymyr.lagom.elasticsearch.deser.ElasticSerializerFactory
import org.taymyr.lagom.elasticsearch.deser.LIST
import org.taymyr.lagom.elasticsearch.search.dsl.SearchRequest
import kotlin.reflect.jvm.javaMethod

/**
 * Lagom service wrapper for [Elasticsearch Search APIs](https://www.elastic.co/guide/en/elasticsearch/reference/current/search.html)
 */
interface ElasticSearch : Service {

    /**
     * Search documents with across multiple types within an index, and across multiple indices.
     * See also [Elasticsearch Docs](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-search.html#search-multi-index-type)
     */
    fun search(indices: List<String>, types: List<String>): ServiceCall<SearchRequest, ByteString>

    @JvmDefault
    override fun descriptor(): Descriptor {
        return named("elastic-search").withCalls(
            restCall<SearchRequest, ByteString>(GET, "/:indices/:types/_search", ElasticSearch::search.javaMethod)
        )
            .withPathParamSerializer(List::class.java, LIST)
            .withSerializerFactory(ElasticSerializerFactory())
    }
}