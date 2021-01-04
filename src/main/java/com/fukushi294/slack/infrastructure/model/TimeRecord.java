package com.fukushi294.slack.infrastructure.model;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Setter
@DynamoDbBean
public class TimeRecord {
    private String id;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @DynamoDbPartitionKey
    public String getId() {
        return this.id;
    }

}
