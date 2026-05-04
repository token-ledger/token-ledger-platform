package com.tokenledgerplatform.io.dto.dashboard;

import java.math.BigDecimal;

public record DashboardKpiResponse(
	BigDecimal totalCost,
	Long totalTokens,
	Long blockedRequests
) {
}