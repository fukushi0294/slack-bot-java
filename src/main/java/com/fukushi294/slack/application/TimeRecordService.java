package com.fukushi294.slack.application;

import com.fukushi294.slack.infrastructure.model.TimeRecord;

import java.util.List;

public interface TimeRecordService {
    void create(TimeRecord timeRecord);

    void update(TimeRecord timeRecord);

    List<TimeRecord> getAllRecord();

    void delete(String id);
}
