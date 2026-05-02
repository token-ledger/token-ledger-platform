package com.ledger.springailedger.dto.dashboard;

import java.math.BigDecimal;

public record ModelCostSummaryResponse(
	String modelId,
	BigDecimal totalCost,
	Long totalTokens
) {
}