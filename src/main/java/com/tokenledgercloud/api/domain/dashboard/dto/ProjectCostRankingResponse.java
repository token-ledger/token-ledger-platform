package com.tokenledgercloud.api.domain.dashboard.dto;

import java.math.BigDecimal;

public record ProjectCostRankingResponse(
	Long projectId,
	BigDecimal totalCost,
	Long totalTokens
) {
}
