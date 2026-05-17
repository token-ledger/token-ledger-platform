package com.tokenledgercloud.api.domain.event.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tokenledgercloud.api.domain.event.dto.OperationalEventItemResponse;
import com.tokenledgercloud.api.domain.event.dto.OperationalEventListResponse;
import com.tokenledgercloud.api.domain.event.entity.OperationalEvent;
import com.tokenledgercloud.api.domain.event.repository.OperationalEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationalEventService {

	private final OperationalEventRepository operationalEventRepository;

	@Transactional(readOnly = true)
	public OperationalEventListResponse getEvents(
		String projectId,
		String environment,
		String type,
		String cursor,
		Integer size
	) {
		validateType(type);

		List<OperationalEvent> events;

		if (projectId != null && !projectId.isBlank()) {
			events = operationalEventRepository.findByProjectIdOrderByOccurredAtDesc(projectId);
		} else if (environment != null && !environment.isBlank()) {
			events = operationalEventRepository.findByEnvironmentOrderByOccurredAtDesc(environment);
		} else if (type != null && !type.isBlank()) {
			events = operationalEventRepository.findByEventTypeOrderByOccurredAtDesc(type);
		} else {
			events = operationalEventRepository.findTop20ByOrderByOccurredAtDesc();
		}

		int limit = size == null ? 20 : Math.min(size, 100);

		List<OperationalEventItemResponse> items = events.stream()
			.limit(limit)
			.map(OperationalEventItemResponse::from)
			.toList();

		String nextCursor = items.isEmpty()
			? null
			: items.get(items.size() - 1).id();

		return new OperationalEventListResponse(items, nextCursor);
	}

	private void validateType(String type) {
		if (type == null || type.isBlank()) {
			return;
		}

		if (!type.equals("INFO") && !type.equals("WARNING") && !type.equals("CRITICAL")) {
			throw new IllegalArgumentException("INVALID_EVENT_TYPE");
		}
	}
}