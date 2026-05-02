package com.ledger.springailedger.dto.dashboard;

import java.math.BigDecimal;

public record DashboardKpiResponse(
	BigDecimal totalCost,
	Long totalTokens,
	Long blockedRequests
) {
}