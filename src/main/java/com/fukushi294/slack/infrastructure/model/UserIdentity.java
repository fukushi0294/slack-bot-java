package com.fukushi294.slack.infrastructure.model;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@DynamoDbBean
public class UserIdentity {
    private String userId;
    private String slackUserId;

    @DynamoDbPartitionKey
    public String getUserId() {
        return this.userId;
    }
}
