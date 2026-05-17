package com.tokenledgercloud.api.domain.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tokenledgercloud.api.domain.event.entity.OperationalEvent;

public interface OperationalEventRepository extends JpaRepository<OperationalEvent, String> {

	List<OperationalEvent> findTop20ByOrderByOccurredAtDesc();

	List<OperationalEvent> findByProjectIdOrderByOccurredAtDesc(String projectId);

	List<OperationalEvent> findByEnvironmentOrderByOccurredAtDesc(String environment);

	List<OperationalEvent> findByEventTypeOrderByOccurredAtDesc(String eventType);
}