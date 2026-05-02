package com.ledger.springailedger.dto.dashboard;

import java.math.BigDecimal;

public record ProjectCostRankingResponse(
	Long projectId,
	BigDecimal totalCost,
	Long totalTokens
) {
}