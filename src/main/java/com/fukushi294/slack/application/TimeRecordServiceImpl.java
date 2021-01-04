package com.fukushi294.slack.application;

import com.fukushi294.slack.infrastructure.model.TimeRecord;
import com.fukushi294.slack.infrastructure.repository.TimeRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeRecordServiceImpl implements TimeRecordService {
    private final TimeRecordRepository timeRecordRepository;

    public TimeRecordServiceImpl(TimeRecordRepository timeRecordRepository) {
        this.timeRecordRepository = timeRecordRepository;
    }

    @Override
    public void create(TimeRecord timeRecord) {

    }

    @Override
    public void update(TimeRecord timeRecord) {

    }

    @Override
    public List<TimeRecord> getAllRecord() {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
