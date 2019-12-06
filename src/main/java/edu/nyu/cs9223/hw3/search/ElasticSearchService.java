package edu.nyu.cs9223.hw3.search;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.*;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
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
    private static String aesEndpoint = "https://vpc-myesdomain-oyovz7rbabj7tzrnaswa5vrnru.us-east-1.es.amazonaws.com";
    private static String index = "photos";

    public static List<PhotoResult> search(String searchString) {
        RestHighLevelClient client = esClient();
        SearchRequest request = new SearchRequest(index)
                .searchType(SearchType.DEFAULT)
                .source(SearchSourceBuilder.searchSource().query(new QueryStringQueryBuilder(searchString)));
        List<PhotoResult> result = new LinkedList<>();
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits()) {
                JsonObject content = JsonParser.parseString(hit.toString()).getAsJsonObject().get("_source").getAsJsonObject();
                String objectKey = content.get("objectKey").getAsString();
                String bucket = content.get("bucket").getAsString();
                String createdTimestamp = content.get("createdTimestamp").getAsString();
                result.add(new PhotoResult(objectKey, bucket, createdTimestamp));
            }
        } catch (IOException e) {
            // do nothing
        }
        return result;
    }

    public static void store(String sampleDocument) throws IOException{
        RestHighLevelClient esClient = esClient();
        // Index a document
        HttpEntity entity = new NStringEntity(sampleDocument, ContentType.APPLICATION_JSON);
        String id = "1";
        IndexRequest request = new IndexRequest(index);
        request.id(id).source(sampleDocument);
        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
    }

    /**
     * Adds the interceptor to the ES REST client
     */
    private static RestHighLevelClient esClient() {
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, new AWSStaticCredentialsProvider(new BasicAWSCredentials("","")));
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }
}
