package com.store.repository;

import com.store.entity.UserData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserData, Long> {
    Mono<UserData> findByUsername(String username);
}
