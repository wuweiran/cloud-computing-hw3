package edu.nyu.cs9223.hw3.search;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhotoResult {
    private String objectKey;
    private String bucket;
    private String createdTimestamp;
}
