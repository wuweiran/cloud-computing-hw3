package edu.nyu.cs9223.hw3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;

import java.util.ArrayList;
import java.util.List;

public class LabelDetector {

 public List<String> detectLables(String bucket, String photo) {
    AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    List<String> res = new ArrayList<>();
    
    DetectLabelsRequest request = new DetectLabelsRequest()
         .withImage(new Image()
         .withS3Object(new S3Object()
         .withName(photo).withBucket(bucket)))
         .withMaxLabels(10)
         .withMinConfidence(75F);

    try {
       DetectLabelsResult result = rekognitionClient.detectLabels(request);
       List <Label> labels = result.getLabels();
       for (Label label: labels) {
          res.add(label.getName());
       }
    } catch(AmazonRekognitionException e) {
       e.printStackTrace();
    }
    
    return res;
 }
}