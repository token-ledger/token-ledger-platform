package com.tokenledgerplatform.io.dto.dashboard;

import java.math.BigDecimal;

public record ModelCostSummaryResponse(
	String modelId,
	BigDecimal totalCost,
	Long totalTokens
) {
}