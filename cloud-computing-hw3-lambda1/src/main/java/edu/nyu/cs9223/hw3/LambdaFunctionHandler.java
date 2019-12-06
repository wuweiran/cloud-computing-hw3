package edu.nyu.cs9223.hw3;

import java.io.IOException;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import edu.nyu.cs9223.hw3.search.ElasticSearchService;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    public LambdaFunctionHandler() {}

    // Test purpose only.
    LambdaFunctionHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);

        // Get the object from the event and show its content type
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        context.getLogger().log("bucket: " + bucket + " key: " + key);
        try {
            LabelDetector ld = new LabelDetector();
            List<String> labels = ld.detectLables(bucket, key);
            for (String label: labels) {
                context.getLogger().log("label: " + label);
             }
            
            String json_str = new Response(key, bucket, labels).toString();
            try{
                ElasticSearchService.store(json_str);
                return "Store in Elastic Search Successfully!";
            } catch(IOException e){
                e.printStackTrace();
                context.getLogger().log("Some problems interacting with ES!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
            throw e;
        }
        
        return "Fail for some reason :(";
        
        
    }
}