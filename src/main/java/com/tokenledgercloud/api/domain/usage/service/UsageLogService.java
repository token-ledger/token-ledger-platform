package com.tokenledgercloud.api.domain.usage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tokenledgercloud.api.domain.usage.dto.UsageLogCreateRequest;
import com.tokenledgercloud.api.domain.usage.dto.UsageLogResponse;
import com.tokenledgercloud.api.domain.usage.entity.UsageLog;
import com.tokenledgercloud.api.domain.usage.repository.UsageLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsageLogService {

	private final UsageLogRepository usageLogRepository;

	@Transactional
	public UsageLogResponse create(UsageLogCreateRequest request) {
		if (request.requestId() != null && !request.requestId().isBlank()) {
			return usageLogRepository.findByProjectIdAndEnvironmentAndRequestId(
					request.projectId(),
					request.environment(),
					request.requestId()
				)
				.map(UsageLogResponse::from)
				.orElseGet(() -> UsageLogResponse.from(saveNew(request)));
		}
		return UsageLogResponse.from(saveNew(request));
	}

	private UsageLog saveNew(UsageLogCreateRequest request) {
		Long totalTokens = request.totalTokens() != null
			? request.totalTokens()
			: request.promptTokens()
				+ request.completionTokens()
				+ safe(request.reasoningTokens())
				+ safe(request.cachedPromptTokens());

		var totalCostUsd = request.totalCostUsd() != null
			? request.totalCostUsd()
			: request.promptCostUsd()
				.add(request.completionCostUsd())
				.add(safe(request.reasoningCostUsd()))
				.add(safe(request.cachedPromptCostUsd()));

		UsageLog usageLog = UsageLog.builder()
			.organizationId(request.organizationId())
			.projectId(request.projectId())
			.apiKeyId(request.apiKeyId())
			.environment(request.environment())
			.requestId(request.requestId())
			.provider(request.provider())
			.model(request.model())
			.promptTokens(request.promptTokens())
			.completionTokens(request.completionTokens())
			.reasoningTokens(safe(request.reasoningTokens()))
			.cachedPromptTokens(safe(request.cachedPromptTokens()))
			.totalTokens(totalTokens)
			.promptCostUsd(request.promptCostUsd())
			.completionCostUsd(request.completionCostUsd())
			.reasoningCostUsd(safe(request.reasoningCostUsd()))
			.cachedPromptCostUsd(safe(request.cachedPromptCostUsd()))
			.totalCostUsd(totalCostUsd)
			.pricingPlanId(request.pricingPlanId())
			.pricingVersion(request.pricingVersion())
			.sourceType(request.sourceType())
			.metadataJson(request.metadataJson())
			.occurredAt(request.occurredAt())
			.build();

		return usageLogRepository.save(usageLog);
	}

	private Long safe(Long value) {
		return value == null ? 0L : value;
	}

	private java.math.BigDecimal safe(java.math.BigDecimal value) {
		return value == null ? java.math.BigDecimal.ZERO : value;
	}
}
