package com.fukushi294.slack.infrastructure.repository;

import com.fukushi294.slack.infrastructure.DataSourceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import javax.annotation.PostConstruct;

@Slf4j
@Repository
public class TimeRecordRepositoryImpl implements TimeRecordRepository {
    @Value("{aws.dynamodb.timeRecord}")
    private String tableName;
    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public TimeRecordRepositoryImpl(DataSourceClient dataSourceClient) {
        this.dynamoDbClient = dataSourceClient.getDynamoDbClient();
        this.dynamoDbEnhancedClient = dataSourceClient.getDynamoDbEnhancedClient();
    }

    @PostConstruct
    public void createTableIfNotExists() {
        DynamoDbWaiter dbWaiter = dynamoDbClient.waiter();
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("id")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build())
                .tableName(tableName)
                .build();
        try {
            dynamoDbClient.createTable(request);
            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(s -> log.info(s.toString()));
        } catch (DynamoDbException e) {
            log.error("Failed to create time record table", e);
        }
    }

}
