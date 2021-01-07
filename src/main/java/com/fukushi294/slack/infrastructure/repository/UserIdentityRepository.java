package com.fukushi294.slack.infrastructure.repository;

import com.fukushi294.slack.infrastructure.model.UserIdentity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserIdentityRepository extends CrudRepository<UserIdentity, String> {
}
