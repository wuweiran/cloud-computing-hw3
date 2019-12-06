package edu.nyu.cs9223.hw3;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.amazonaws.protocol.json.SdkJsonGenerator.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class Response implements Serializable{
    String objectKey;
    String bucket;
    Timestamp createdTimestamp;
    List<String> labels;
    
    public Response(String objectKey, String bucket, List<String> labels){
        this.objectKey = objectKey;
        this.bucket = bucket;
        this.createdTimestamp = new Timestamp(System.currentTimeMillis());
        this.labels = labels;
    }
    
    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
