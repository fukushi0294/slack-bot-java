package com.fukushi294.slack.infrastructure.repository;

import com.fukushi294.slack.infrastructure.model.TimeRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface TimeRecordRepository extends CrudRepository<TimeRecord, String> {
    List<TimeRecord> findByUserId(String userId);
}
