package com.ledger.springailedger.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ledger.springailedger.domain.usage.UsageLog;
import com.ledger.springailedger.domain.usage.UsageLogRepository;
import com.ledger.springailedger.dto.usage.UsageLogCreateRequest;
import com.ledger.springailedger.dto.usage.UsageLogResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsageLogService {

	private final UsageLogRepository usageLogRepository;

	@Transactional
	public UsageLogResponse create(UsageLogCreateRequest request) {
		return usageLogRepository.findByIdempotencyKey(request.idempotencyKey())
			.map(UsageLogResponse::from)
			.orElseGet(() -> UsageLogResponse.from(saveNew(request)));
	}

	private UsageLog saveNew(UsageLogCreateRequest request) {
		Long totalTokens = request.totalTokens() != null
			? request.totalTokens()
			: request.inputTokens() + request.outputTokens();

		UsageLog usageLog = UsageLog.builder()
			.eventId(request.eventId())
			.idempotencyKey(request.idempotencyKey())
			.projectId(request.projectId())
			.applicationId(request.applicationId())
			.userId(request.userId())
			.modelId(request.modelId())
			.inputTokens(request.inputTokens())
			.outputTokens(request.outputTokens())
			.totalTokens(totalTokens)
			.totalCost(request.totalCost())
			.currencyCode(request.currencyCode() == null ? "USD" : request.currencyCode())
			.status(request.status())
			.startedAt(request.startedAt())
			.finishedAt(request.finishedAt())
			.latencyMs(request.latencyMs())
			.errorCode(request.errorCode())
			.errorMessage(request.errorMessage())
			.build();

		return usageLogRepository.save(usageLog);
	}
}