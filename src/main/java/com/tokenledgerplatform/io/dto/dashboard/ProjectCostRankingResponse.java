package com.tokenledgerplatform.io.dto.dashboard;

import java.math.BigDecimal;

public record ProjectCostRankingResponse(
	Long projectId,
	BigDecimal totalCost,
	Long totalTokens
) {
}