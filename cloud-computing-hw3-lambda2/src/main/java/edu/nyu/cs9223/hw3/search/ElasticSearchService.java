package edu.nyu.cs9223.hw3.search;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wwrus
 */
public class ElasticSearchService {

    private static String serviceName = "es";
    private static String region = "us-east-1";
    private static String aesEndpoint = "https://search-yelp-dugc65brzvoxpv5faqvjcpev5e.us-east-1.es.amazonaws.com";
    private static String index = "yelp";

    public static List<String> search(String searchString) {
        RestHighLevelClient client = esClient();
        SearchRequest request = new SearchRequest(index)
                .searchType(SearchType.DEFAULT)
                .source(SearchSourceBuilder.searchSource().query(new QueryStringQueryBuilder(searchString)));
        List<String> result = new LinkedList<>();
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits()) {
                JsonObject content = JsonParser.parseString(hit.toString()).getAsJsonObject().getAsJsonObject("_source");
                result.add("?????");
            }

        } catch (IOException e) {
            // do nothing
        }
        return result;
    }

    /**
     * Adds the interceptor to the ES REST client
     */
    private static RestHighLevelClient esClient() {
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, new AWSStaticCredentialsProvider(new BasicAWSCredentials("AKIAZPF2QUV4IH3NSPPA", "ffRULkFSd0GbzdJR5p6AU8+c4NcGC5JzwrbILqLJ")));
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }
}
